package creational.abstractFactory.factories;


import creational.abstractFactory.buttons.Button;
import creational.abstractFactory.checkboxes.Checkbox;

/**
 * Abstract factory knows about all (abstract) product types.
 */
public interface GUIFactory {
    Button createButton();

    Checkbox createCheckbox();
}