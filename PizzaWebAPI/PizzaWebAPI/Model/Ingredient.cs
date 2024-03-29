﻿using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using System.Linq;
using System.Threading.Tasks;

namespace PizzaWebAPI.Model
{
    [Table("ingredients")]
    public class Ingredient
    {
        [Key]
        public int Id { get; set; }
        public String Name { get; set; }
        public Decimal Price { get; set; }
        public virtual ICollection<MenuItem_Ingredient> MenuItemIngredients { get; set; }
        public virtual ICollection<OrderMenuItem_Ingredient> OrderMenuItemIngredients { get; set; }
    }
}
