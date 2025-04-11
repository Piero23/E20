package demacs.unical.esse20;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class Test implements CommandLineRunner {

    UserDao userDao ;

    public Test (UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public void run(String... args) throws Exception {

        Utente user = new Utente();

        user.setUsername("Raspda");
        user.setEmail("cia@ndas.com");

        try {userDao.save(user);} catch (Exception e) {System.out.println("non è andato");}
        userDao.findAll().forEach(u -> System.out.println(u.getUsername()));
    }
}
