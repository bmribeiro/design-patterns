package creational.abstractFactory.factories;

import creational.abstractFactory.buttons.Button;
import creational.abstractFactory.buttons.WindowsButton;
import creational.abstractFactory.checkboxes.Checkbox;
import creational.abstractFactory.checkboxes.WindowsCheckbox;

/**
 * Each concrete factory extends basic factory and responsible for creating
 * products of a single variety.
 */
public class WindowsFactory implements GUIFactory {

    @Override
    public Button createButton() {
        return new WindowsButton();
    }

    @Override
    public Checkbox createCheckbox() {
        return new WindowsCheckbox();
    }
}