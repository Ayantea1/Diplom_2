import com.github.javafaker.Faker;

public class UserGenerator {
    Faker faker = new Faker();
    public User random() {return new User(faker.internet().emailAddress(),faker.pokemon().name(),faker.name().firstName());}

    public User noEmail() {return new User(null,faker.pokemon().name(),faker.name().firstName());}

}
