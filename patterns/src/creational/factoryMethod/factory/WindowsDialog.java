package creational.factoryMethod.factory;

import creational.factoryMethod.buttons.Button;
import creational.factoryMethod.buttons.WindowsButton;

/**
 * Windows Dialog will produce Windows buttons.
 */
public class WindowsDialog extends Dialog {

    @Override
    public Button createButton() {
        return new WindowsButton();
    }
}