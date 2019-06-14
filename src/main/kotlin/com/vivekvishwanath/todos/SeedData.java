package com.vivekvishwanath.todos;

import com.vivekvishwanath.todos.model.Role;
import com.vivekvishwanath.todos.model.Todo;
import com.vivekvishwanath.todos.model.User;
import com.vivekvishwanath.todos.model.UserRoles;
import com.vivekvishwanath.todos.repository.RoleRepository;
import com.vivekvishwanath.todos.repository.ToDoRepository;
import com.vivekvishwanath.todos.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;

@Transactional
@Component
public class SeedData implements CommandLineRunner {
    RoleRepository rolerepos;
    UserRepository userrepos;
    ToDoRepository todorepos;

    public SeedData(RoleRepository rolerepos, UserRepository userrepos, ToDoRepository todorepos) {
        this.rolerepos = rolerepos;
        this.userrepos = userrepos;
        this.todorepos = todorepos;
    }

    @Override
    public void run(String[] args) throws Exception {
        Role r1 = new Role("admin");
        Role r2 = new Role("user");

        ArrayList<UserRoles> admins = new ArrayList<>();
        admins.add(new UserRoles(new User(), r1));
        admins.add(new UserRoles(new User(), r2));

        ArrayList<UserRoles> users = new ArrayList<>();
        users.add(new UserRoles(new User(), r2));

        rolerepos.save(r1);
        rolerepos.save(r2);

        User u1 = new User("barnbarn", "password", users);
        User u2 = new User("admin", "password", admins);

        // the date and time string should get coverted to a datetime Java data type. This is done in the constructor!
        u1.getTodos().add(new Todo("Finish java-orders-swagger", new Date(), u1));
        u1.getTodos().add(new Todo("Feed the turtles", new Date(), u1));
        u1.getTodos().add(new Todo("Complete the sprint challenge", new Date(), u1));

        u2.getTodos().add(new Todo("Walk the dogs", new Date(), u2));
        u2.getTodos().add(new Todo("provide feedback to my instructor", new Date(), u2));

        userrepos.save(u1);
        userrepos.save(u2);
    }
}
