﻿using Microsoft.EntityFrameworkCore;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace PizzaWebAPI.Model
{
    public class ModelContext : DbContext
    {
        public ModelContext(DbContextOptions<ModelContext> options) : base(options)
        {

        }

        protected override void OnModelCreating(ModelBuilder builder) {
            builder.Entity<Account>()
                 .HasIndex(a => a.Login)
                 .IsUnique();
            builder.Entity<Account>()
                 .HasIndex(a => a.Email)
                 .IsUnique();
        }

        public DbSet<Account> Accounts { get; set; }
        public DbSet<AccountRole> AccountsRoles { get; set; }
        public DbSet<EmployeeData> EmployeesData { get; set; }
        public DbSet<Ingredient> Ingredients { get; set; }
        public DbSet<MenuItem> MenuItems { get; set; }
        public DbSet<MenuItem_Ingredient> MenuItems_Ingredients { get; set; }
        public DbSet<MenuItemOption> MenuItemOptions { get; set; }
        public DbSet<OrderMenuItem> OrderMenuItems { get; set; }
        public DbSet<OrderMenuItem_Ingredient> OrderMenuItems_Ingredients { get; set; }
        public DbSet<Order> Orders { get; set; }
        public DbSet<Restaurant> Restauraunts { get; set; }
        public DbSet<Status> Statuses { get; set; }
        public DbSet<PaymentType> PaymentTypes { get; set; }
    }
}
