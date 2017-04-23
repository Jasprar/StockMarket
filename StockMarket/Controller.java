package StockMarket;


public class Controller {
        Simulator sim = new Simulator(); // dummy test need to get duration then instantiate(?) // need to overrride it
// Essentially, what I think needs to happen is Main's main() method needs to run, it needs to create the GUI and the simulator
// (Which no longer takes any input parameters). Then, when the user of the GUI hits run or start or whatever, a pop-up box appears stating
// "How long (in minutes) would you like the simulation to run for?". Then main calls Simulator.runSimulation(int minutes) and off we go.
// - Jasper.
}
