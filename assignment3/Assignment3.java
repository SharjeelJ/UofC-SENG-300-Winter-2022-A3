/**
 * Sharjeel Junaid
 * 30008424
 */

package assignment3;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.lsmr.selfcheckout.Item;
import org.lsmr.selfcheckout.devices.AbstractDevice;
import org.lsmr.selfcheckout.devices.ElectronicScale;
import org.lsmr.selfcheckout.devices.OverloadException;
import org.lsmr.selfcheckout.devices.SimulationException;
import org.lsmr.selfcheckout.devices.observers.AbstractDeviceObserver;
import org.lsmr.selfcheckout.devices.observers.ElectronicScaleObserver;

import static org.junit.Assert.assertEquals;

// Main class that contains all the test cases for the functionality of the Electronic Scale class
@RunWith(Suite.class)
@Suite.SuiteClasses({Assignment3.ElectronicScaleCoreFunctionalityTests.class, Assignment3.ElectronicScaleAddItemTests.class, Assignment3.ElectronicScaleRemoveItemTests.class, Assignment3.ElectronicScaleGetWeightTests.class, Assignment3.ElectronicScaleNotifyObserverTests.class})
public class Assignment3
{
    // Inner class that contains all tests for the core functionality of the Electronic Scale (constructing it and simple functions for getting data)
    public static class ElectronicScaleCoreFunctionalityTests
    {
        // Declare the electronic scale will be initialized and used by the test cases
        ElectronicScale electronicScale;

        // Initialize global static variables that will be used by the test cases
        final int scaleWeightLimit = 10;
        final int scaleSensitivity = 1;

        // Tests to see if the documented weight limit (has to be greater than zero) throws the specified error if not satisfied
        @Test(expected = SimulationException.class)
        public void testScaleConstructorWeightLimitRequirement()
        {
            this.electronicScale = new ElectronicScale(0, scaleSensitivity);
        }

        // Tests to see if the documented sensitivity (has to be greater than zero) throws the specified error if not satisfied
        @Test(expected = SimulationException.class)
        public void testScaleConstructorSensitivityRequirement()
        {
            this.electronicScale = new ElectronicScale(scaleWeightLimit, 0);
        }

        // Tests to see if the scale can be constructed successfully given valid input (both values greater than zero)
        @Test
        public void testScaleConstructorUsingValidInput()
        {
            this.electronicScale = new ElectronicScale(scaleWeightLimit, scaleSensitivity);
        }

        // Tests to see if the weight limit can successfully and correctly be obtained
        @Test
        public void testGetWeightLimit()
        {
            this.electronicScale = new ElectronicScale(scaleWeightLimit, scaleSensitivity);
            assertEquals(electronicScale.getWeightLimit(), scaleWeightLimit, 0);
        }

        // Tests to see if the sensitivity can successfully and correctly be obtained
        @Test
        public void testGetSensitivity()
        {
            this.electronicScale = new ElectronicScale(scaleWeightLimit, scaleSensitivity);
            assertEquals(electronicScale.getSensitivity(), scaleSensitivity, 0);
        }
    }

    // Inner class that contains all tests for the add item functionality of the Electronic Scale
    public static class ElectronicScaleAddItemTests
    {
        // Declare variables that will be initialized and used by the test cases
        ElectronicScale electronicScale;
        Item item1;
        Item item2;

        // Initialize global static variables that will be used by the test cases
        final int scaleWeightLimit = 10;
        final int scaleSensitivity = 1;
        final double item1Weight = 5.25;
        final double item2Weight = 4.50;

        // Initializes the electronic scale and the two test items before each test
        @Before
        public void setup()
        {
            this.electronicScale = new ElectronicScale(scaleWeightLimit, scaleSensitivity);
            this.item1 = new Item(item1Weight) {};
            this.item2 = new Item(item2Weight) {};
        }

        // Tests to see if an error is correctly thrown when attempting to add an item during the configuration phase
        @Test(expected = SimulationException.class)
        public void testAddingDuringConfigPhase()
        {
            electronicScale.add(item1);
        }

        // Tests to see if an error is correctly thrown when attempting to add an item during the error phase
        @Test(expected = SimulationException.class)
        public void testAddingDuringErrorPhase()
        {
            electronicScale.forceErrorPhase();
            electronicScale.add(item1);
        }

        // Tests adding an item after the configuration phase
        @Test
        public void testAddingItemAfterConfigPhase()
        {
            electronicScale.endConfigurationPhase();
            electronicScale.add(item1);
        }

        // Tests to see if an error is correctly thrown when attempting to add the same item twice after the configuration phase
        @Test(expected = SimulationException.class)
        public void testAddingDuplicateItemAfterConfigPhase()
        {
            electronicScale.endConfigurationPhase();
            electronicScale.add(item1);
            electronicScale.add(item1);
        }

        // Tests adding two different item after the configuration phase
        @Test
        public void testAddingTwoDifferentItemsAfterConfigPhase()
        {
            electronicScale.endConfigurationPhase();
            electronicScale.add(item1);
            electronicScale.add(item2);
        }

        // Tests adding an item that is above the weight limit after the configuration phase
        @Test
        public void testAddingOverweightItemAfterConfigPhase()
        {
            electronicScale.endConfigurationPhase();
            item1 = new Item(scaleWeightLimit + scaleSensitivity) {};
            electronicScale.add(item1);
        }
    }

    // Inner class that contains all tests for the remove item functionality of the Electronic Scale
    public static class ElectronicScaleRemoveItemTests
    {
        // Declare variables that will be initialized and used by the test cases
        ElectronicScale electronicScale;
        Item item1;
        Item item2;

        // Initialize global static variables that will be used by the test cases
        final int scaleWeightLimit = 10;
        final int scaleSensitivity = 1;
        final double item1Weight = 5.25;
        final double item2Weight = 4.50;

        // Initializes the electronic scale and the two test items before each test
        @Before
        public void setup()
        {
            this.electronicScale = new ElectronicScale(scaleWeightLimit, scaleSensitivity);
            this.item1 = new Item(item1Weight) {};
            this.item2 = new Item(item2Weight) {};
        }

        // Tests to see if an error is correctly thrown when attempting to remove an item during the configuration phase
        @Test(expected = SimulationException.class)
        public void testRemoveDuringConfigPhase()
        {
            electronicScale.remove(item1);
        }

        // Tests to see if an error is correctly thrown when attempting to remove an item during the error phase
        @Test(expected = SimulationException.class)
        public void testRemoveDuringErrorPhase()
        {
            electronicScale.forceErrorPhase();
            electronicScale.remove(item1);
        }

        // Tests to see if an error is correctly thrown when attempting to remove an item that was not added after the configuration phase
        @Test(expected = SimulationException.class)
        public void testRemoveUnaccountedItemAfterConfigPhase()
        {
            electronicScale.endConfigurationPhase();
            electronicScale.remove(item1);
        }

        // Tests removing an item after the configuration phase
        @Test
        public void testRemovingItemAfterConfigPhase()
        {
            electronicScale.endConfigurationPhase();
            electronicScale.add(item1);
            electronicScale.remove(item1);
        }

        // Tests removing two different items after the configuration phase
        @Test
        public void testRemovingTwoDifferentItemsAfterConfigPhase()
        {
            electronicScale.endConfigurationPhase();
            electronicScale.add(item1);
            electronicScale.add(item2);
            electronicScale.remove(item1);
            electronicScale.remove(item2);
        }

        // Tests removing an overweight item after the configuration phase
        @Test
        public void testRemovingOverweightItemAfterConfigPhase()
        {
            electronicScale.endConfigurationPhase();
            item1 = new Item(scaleWeightLimit + scaleSensitivity) {};
            electronicScale.add(item1);
            electronicScale.remove(item1);
        }
    }

    // Inner class that contains all tests for the get weight on the scale functionality of the Electronic Scale
    public static class ElectronicScaleGetWeightTests
    {
        // Declare variables that will be initialized and used by the test cases
        ElectronicScale electronicScale;
        Item item1;
        Item item2;

        // Initialize global static variables that will be used by the test cases
        final int scaleWeightLimit = 10;
        final int scaleSensitivity = 1;
        final double item1Weight = 5.25;
        final double item2Weight = 4.50;

        // Initializes the electronic scale and the two test items before each test
        @Before
        public void setup()
        {
            this.electronicScale = new ElectronicScale(scaleWeightLimit, scaleSensitivity);
            this.item1 = new Item(item1Weight) {};
            this.item2 = new Item(item2Weight) {};
        }

        // Tests to see if an error is correctly thrown when attempting to check the current weight on the scale during the configuration phase
        @Test(expected = SimulationException.class)
        public void testGetCurrentWeightDuringConfigPhase() throws OverloadException
        {
            electronicScale.getCurrentWeight();
        }

        // Tests to see if an error is correctly thrown when attempting to check the current weight on the scale during the error phase
        @Test(expected = SimulationException.class)
        public void testGetCurrentWeightDuringErrorPhase() throws OverloadException
        {
            electronicScale.forceErrorPhase();
            electronicScale.getCurrentWeight();
        }

        // Tests getting the current weight on the scale after the configuration phase
        @Test
        public void testGetCurrentWeightAfterConfigPhase() throws OverloadException
        {
            electronicScale.endConfigurationPhase();
            assertEquals(electronicScale.getCurrentWeight(), 0, scaleSensitivity);
        }

        // Tests getting the current weight on the scale after adding an item after the configuration phase
        @Test
        public void testGetCurrentWeightWithItemAfterConfigPhase() throws OverloadException
        {
            electronicScale.endConfigurationPhase();
            electronicScale.add(item1);
            assertEquals(electronicScale.getCurrentWeight(), item1Weight, scaleSensitivity);
        }

        // Tests getting the current weight on the scale after adding and removing an item after the configuration phase
        @Test
        public void testGetCurrentWeightWithoutItemAfterConfigPhase() throws OverloadException
        {
            electronicScale.endConfigurationPhase();
            electronicScale.add(item1);
            electronicScale.remove(item1);
            assertEquals(electronicScale.getCurrentWeight(), 0, scaleSensitivity);
        }

        // Tests getting the current weight on the scale after adding an overweight item after the configuration phase
        @Test(expected = OverloadException.class)
        public void testGetCurrentWeightWithOverweightItemAfterConfigPhase() throws OverloadException
        {
            electronicScale.endConfigurationPhase();
            item1 = new Item(scaleWeightLimit + scaleSensitivity) {};
            electronicScale.add(item1);
            electronicScale.getCurrentWeight();
        }

        // Tests getting the current weight on the scale after adding and removing an overweight item after the configuration phase
        @Test
        public void testGetCurrentWeightWithoutOverweightItemAfterConfigPhase() throws OverloadException
        {
            electronicScale.endConfigurationPhase();
            item1 = new Item(scaleWeightLimit + scaleSensitivity) {};
            electronicScale.add(item1);
            electronicScale.remove(item1);
            assertEquals(electronicScale.getCurrentWeight(), 0, scaleSensitivity);
        }

        // Tests getting the weight on the scale after adding and removing two items (without causing an overweight exception) after the configuration phase
        @Test
        public void testWeightAddingAndRemovingItemsWithoutCausingOverweightAfterConfigPhase() throws OverloadException
        {
            electronicScale.endConfigurationPhase();
            assertEquals(electronicScale.getCurrentWeight(), 0, scaleSensitivity);
            electronicScale.add(item1);
            assertEquals(electronicScale.getCurrentWeight(), item1Weight, scaleSensitivity);
            electronicScale.add(item2);
            assertEquals(electronicScale.getCurrentWeight(), item1Weight + item2Weight, scaleSensitivity);
            electronicScale.remove(item1);
            assertEquals(electronicScale.getCurrentWeight(), item2Weight, scaleSensitivity);
            electronicScale.remove(item2);
            assertEquals(electronicScale.getCurrentWeight(), 0, scaleSensitivity);
        }

        // Tests getting the weight on the scale after adding and removing two items (while causing an overweight exception) after the configuration phase
        @Test
        public void testWeightAddingAndRemovingItemsCausingOverweightAfterConfigPhase() throws OverloadException
        {
            electronicScale.endConfigurationPhase();
            item2 = new Item(scaleWeightLimit - item1Weight + scaleSensitivity) {};
            assertEquals(electronicScale.getCurrentWeight(), 0, scaleSensitivity);
            electronicScale.add(item1);
            assertEquals(electronicScale.getCurrentWeight(), item1Weight, scaleSensitivity);
            electronicScale.add(item2);
            try
            {
                assertEquals(electronicScale.getCurrentWeight(), item1Weight + item2Weight, scaleSensitivity);
            } catch (OverloadException ignored)
            {

            }
            electronicScale.remove(item1);
            assertEquals(electronicScale.getCurrentWeight(), scaleWeightLimit - item1Weight + scaleSensitivity, scaleSensitivity);
            electronicScale.remove(item2);
            assertEquals(electronicScale.getCurrentWeight(), 0, scaleSensitivity);
        }
    }

    // Inner class that contains all tests for the notify observer functionality of the Electronic Scale
    public static class ElectronicScaleNotifyObserverTests
    {
        // Declare variables that will be initialized and used by the test cases
        ElectronicScale electronicScale;
        ElectronicScaleObserver electronicScaleObserver;
        Item item1;
        Item item2;

        // Initialize global static variables that will be used by the test cases
        final int scaleWeightLimit = 10;
        final int scaleSensitivity = 1;
        final double item1Weight = 5.25;
        final double item2Weight = 4.50;

        // Initializes the electronic scale, its observer (and attaches it to the electronic scale) and the two test items before each test
        @Before
        public void setup()
        {
            this.electronicScale = new ElectronicScale(scaleWeightLimit, scaleSensitivity);
            this.electronicScaleObserver = new ElectronicScaleObserver()
            {
                @Override
                public void weightChanged(ElectronicScale scale, double weightInGrams) {}

                @Override
                public void overload(ElectronicScale scale) {}

                @Override
                public void outOfOverload(ElectronicScale scale) {}

                @Override
                public void enabled(AbstractDevice <? extends AbstractDeviceObserver> device) {}

                @Override
                public void disabled(AbstractDevice <? extends AbstractDeviceObserver> device) {}
            };
            this.item1 = new Item(item1Weight) {};
            this.item2 = new Item(item2Weight) {};
            this.electronicScale.attach(electronicScaleObserver);
        }

        // Tests adding an item after the configuration phase
        @Test
        public void testAddingItemAfterConfigPhase()
        {
            electronicScale.endConfigurationPhase();
            electronicScale.add(item1);
        }

        // Tests adding an item that is above the weight limit after the configuration phase
        @Test
        public void testAddingOverweightItemAfterConfigPhase()
        {
            electronicScale.endConfigurationPhase();
            item1 = new Item(scaleWeightLimit + scaleSensitivity) {};
            electronicScale.add(item1);
        }

        // Tests removing an item after the configuration phase
        @Test
        public void testRemovingItemAfterConfigPhase()
        {
            electronicScale.endConfigurationPhase();
            electronicScale.add(item1);
            electronicScale.remove(item1);
        }

        // Tests removing an overweight item after the configuration phase
        @Test
        public void testRemovingOverweightItemAfterConfigPhase()
        {
            electronicScale.endConfigurationPhase();
            item1 = new Item(scaleWeightLimit + scaleSensitivity) {};
            electronicScale.add(item1);
            electronicScale.remove(item1);
        }
    }
}
