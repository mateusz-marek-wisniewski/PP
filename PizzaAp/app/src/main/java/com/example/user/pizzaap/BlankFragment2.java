package com.example.user.pizzaap;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Checkable;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import static java.lang.System.in;


public class BlankFragment2 extends Fragment {


    public BlankFragment2() {
        // Required empty public constructor
    }
    Context context;
    View view;
    ArrayList<HashMap<String, String>> MenuItems = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> MenuItemsIngredients = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> IngredientsList = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>>  selectedItems = new ArrayList<>();


    ListView lV;
    ListAdapter adapter;
    String q ="";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        return inflater.inflate(R.layout.fragment_blank_fragment2, container, false);
    }
    ListView IngredientsView;
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Button btn = view.findViewById(R.id.PizzaButton);
        lV = view.findViewById(R.id.ListOfPizzas);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lV.setAdapter(null);
                MenuItems = new ArrayList<>();
                context = v.getContext();
                int id = Restaurant.Id;
                q = "/" + String.valueOf(id);
                new GetContacts().execute();

            }
        });
        lV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                view.post(new Runnable() {
                    @Override
                    public void run() {
                        selectedItems.clear();
                        Toast.makeText(getActivity(), "TESTING BUTTON CLICK 1",Toast.LENGTH_SHORT).show();
                        final Dialog dialog = new Dialog(context);
                        dialog.setContentView(R.layout.custom_dialog);
                        dialog.setTitle("Title...");
                        IngredientsView = (ListView) dialog.findViewById(R.id.ListIngredients);
                        IngredientsView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

                        HashMap<String, String> MenuItemId = (HashMap<String, String>) lV.getItemAtPosition(position);
                        for (int j = 0; j < IngredientsList.size(); j++) {

                            for (int i = 0; i < MenuItemsIngredients.size(); i++) {
                                if (IngredientsList.get(j).get("id") == MenuItemsIngredients.get(i).get("ingredientId") && MenuItemId.get("id") == MenuItemsIngredients.get(i).get("MenuId")) {
                                    HashMap<String, String> temp = new HashMap<>();
                                    temp.put("id", MenuItemsIngredients.get(i).get("ingredientId"));
                                    temp.put("name", IngredientsList.get(j).get("name"));
                                    temp.put("price", IngredientsList.get(j).get("price"));

                                    selectedItems.add(temp);

                                }

                            }
                        }
                        adapter = new SimpleAdapter( context, IngredientsList,
                                R.layout.checkable_list_layout, new String[]{"name"
                        }, new int[]{R.id.txt_title}) {
                            @Override
                            public View getView(int position, View convertView, ViewGroup parent) {

                                // Get the current item from ListView
                                View view = super.getView(position, convertView, parent);
                                HashMap<String, String> selectedItem =  (HashMap<String, String>)IngredientsView.getItemAtPosition(position);
                               if(selectedItems.contains(selectedItem))
                               {
                                   view.setBackgroundColor(Color.CYAN);
                               }
                                return view;
                            }
                        };


                        IngredientsView.setAdapter(adapter);

                        IngredientsView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                try {



                                    HashMap<String, String> selectedItem =  (HashMap<String, String>)IngredientsView.getItemAtPosition(position);
                                    if (selectedItems.contains(selectedItem)) {

                                        IngredientsView.setItemChecked(position,false);
                                      //  IngredientsView.setBackgroundColor(Color.parseColor("#FFFFFF"));
                                        view.setBackgroundColor(Color.WHITE);

                                        selectedItems.remove(selectedItem); //remove deselected item from the list of selected items
                                    }
                                    else {
                                        IngredientsView.setItemChecked(position, true);
                                        selectedItems.add(selectedItem); //add selected item to the list of selected items
                                     //   IngredientsView.setBackgroundColor(Color.parseColor("#FFFFAF"));
                                        view.setBackgroundColor(Color.CYAN);
                                    }
                                }
                                catch (final Exception E)
                                {
                                    Log.e("CPS", "Json parsing error: " + E.getMessage());
                                    view.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(getActivity(),
                                                    "Json parsing error: " + E.getMessage(),
                                                    Toast.LENGTH_LONG)
                                                    .show();
                                        }
                                    });
                                }

                            }


                        });
                        dialog.show();

                    }
                });

            }
        });

    }


    private class GetContacts extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
        }

        @Override
        protected Void doInBackground(Void... arg0) {


BackGroundRestaurants(ApiStrings.RestaurantsURL + q);
BackGroundAllIngredients(ApiStrings.Ingredients);
for(int i=0;i<MenuItems.size();i++)
{
BackGroundMenu(ApiStrings.MenuItems+MenuItems.get(i).get("id"),MenuItems.get(i).get("id"));
}
            return null;
        }
        private void BackGroundRestaurants(String url) {
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url);

            Log.e("cos", "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    JSONArray Restaurants = jsonObj.getJSONArray("menuItems");

                    // Getting JSON Array node
                    //JSONArray Restaurants = new JSONArray(jsonStr);

                    // looping through All Contacts
                    for (int i = 0; i < Restaurants.length(); i++) {
                        JSONObject d = Restaurants.getJSONObject(i);

                        String id = d.getString("id");
                        String name = d.getString("name");
                        //   JSONArray distan = jsonObj.getJSONArray("distance");
                        //  String distance = distan.getString("distance");
                        HashMap<String, String> contact = new HashMap<>();


                        // adding each child node to HashMap key => value


                        contact.put("id", id);
                        contact.put("name", name);


                        // adding contact to contact list
                        MenuItems.add(contact);
                    }
                } catch (final Exception e) {
                    Log.e("doc", "Json parsing error: " + e.getMessage());
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity().getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });

                }


            } else {
                Log.e("cld", "Couldn't get json from server.");
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity().getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });

            }
        }
        private void BackGroundMenu(String url,String MenuId) {
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url);

            Log.e("cos", "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    JSONArray Restaurants = jsonObj.getJSONArray("menuItemIngredients");

                    // Getting JSON Array node
                    //JSONArray Restaurants = new JSONArray(jsonStr);

                    // looping through All Contacts
                    for (int i = 0; i < Restaurants.length(); i++) {
                        JSONObject d = Restaurants.getJSONObject(i);

                        String id = d.getString("ingredientId");


                        //   JSONArray distan = jsonObj.getJSONArray("distance");
                        //  String distance = distan.getString("distance");
                        HashMap<String, String> contact = new HashMap<>();


                        // adding each child node to HashMap key => value
                        contact.put("ingredientId", id);

                        contact.put("MenuId",MenuId);


                        // adding contact to contact list
                        MenuItemsIngredients.add(contact);
                    }
                } catch (final Exception e) {
                    Log.e("doc", "Json parsing error: " + e.getMessage());
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity().getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });

                }


            } else {
                Log.e("cld", "Couldn't get json from server.");
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity().getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });

            }
        }
        private void BackGroundAllIngredients(String URL)
        {
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(URL);

            Log.e("cos", "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {

                    JSONArray Restaurants = new JSONArray(jsonStr);

                    // Getting JSON Array node
                    //JSONArray Restaurants = new JSONArray(jsonStr);

                    // looping through All Contacts
                    for (int i = 0; i < Restaurants.length(); i++) {
                        JSONObject d = Restaurants.getJSONObject(i);

                        String id = d.getString("id");
                        String name = d.getString("name");
                        String price = d.getString("price");
                        //   JSONArray distan = jsonObj.getJSONArray("distance");
                        //  String distance = distan.getString("distance");
                        HashMap<String, String> contact = new HashMap<>();

                        // adding each child node to HashMap key => value
                        contact.put("id", id);
                        contact.put("name", name);
                        contact.put("price",price);


                        // adding contact to contact list
                        IngredientsList.add(contact);
                    }
                } catch (final Exception e) {
                    Log.e("doc", "Json parsing error: " + e.getMessage());
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity().getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });

                }


            } else {
                Log.e("cld", "Couldn't get json from server.");
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity().getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });
            }
            }


        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog

            /**
             * Updating parsed JSON data into ListView
             * */
            try {
            ListAdapter adapter = new SimpleAdapter(
                    context, MenuItems,
                    R.layout.addd, new String[]{"name", "id"
            }, new int[]{R.id.PizzaName, R.id.PizzaId});

            lV.setAdapter(adapter);


            }catch (Exception p)
            {
                Log.e("ADAD",p.toString());
            }

        }
    }

}
