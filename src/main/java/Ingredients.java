import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Ingredients {
    public  ArrayList<String> ingredients;

    public Ingredients(ArrayList<String> ingredients) {
        this.ingredients = ingredients;
    }

    public Ingredients() {
    }

    public ArrayList<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(ArrayList<String> ingredients) {
        this.ingredients = ingredients;
    }
}
