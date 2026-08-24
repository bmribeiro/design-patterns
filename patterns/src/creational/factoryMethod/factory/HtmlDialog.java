package creational.factoryMethod.factory;

import creational.factoryMethod.buttons.Button;
import creational.factoryMethod.buttons.HtmlButton;

/**
 * HTML Dialog will produce HTML buttons.
 */
public class HtmlDialog extends Dialog {

    @Override
    public Button createButton() {
        return new HtmlButton();
    }
}