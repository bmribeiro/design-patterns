package creational.abstractFactory.factories;

import creational.abstractFactory.buttons.Button;
import creational.abstractFactory.buttons.MacOSButton;
import creational.abstractFactory.checkboxes.Checkbox;
import creational.abstractFactory.checkboxes.MacOSCheckbox;

/**
 * Each concrete factory extends basic factory and responsible for creating
 * products of a single variety.
 */
public class MacOSFactory implements GUIFactory {

    @Override
    public Button createButton() {
        return new MacOSButton();
    }

    @Override
    public Checkbox createCheckbox() {
        return new MacOSCheckbox();
    }
}