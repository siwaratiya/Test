import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.edge.EdgeDriver;

import java.util.List;

public class expl {

    public static void main(String[] args) {
        WebDriver driver = new EdgeDriver();

            driver.get("https://www.laredoute.fr/pplp/100/75363/158090/75502/cat-75508.aspx#shoppingtool=treestructureguidednavigation");


        WebElement but = driver.findElement(By.id("popin_tc_privacy_button_2"));
        but.click();


        // Get element with tag name 'div'
            WebElement element = driver.findElement(By.xpath("//*[@id=\"productList\"]"));

            // Get all the elements available with tag name 'p'
            List<WebElement> elements = element.findElements(By.tagName("li"));
            for (WebElement e : elements) {

                System.out.println(e.getText());
            }


            }

}
