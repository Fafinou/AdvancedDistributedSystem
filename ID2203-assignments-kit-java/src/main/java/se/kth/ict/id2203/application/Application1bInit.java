/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.ict.id2203.application;
import java.util.Set;
import se.sics.kompics.Init;
import se.sics.kompics.address.Address;

/**
 *
 * @author fingolfin
 */
public class Application1bInit extends Init {
    	private final String commandScript;

	private final Set<Address> neighborSet;
	
	private final Address self;
	
	/**
	 * Instantiates a new application0 init.
	 * 
	 * @param commandScript
	 *            the command script
	 * @param neighborSet
	 *            the neighbor set
	 * @param self
	 *            the self
	 */
	public Application1bInit(String commandScript, Set<Address> neighborSet, Address self) {
		super();
		this.commandScript = commandScript;
		this.neighborSet = neighborSet;
		this.self = self;
	}
	
	/**
	 * Gets the command script.
	 * 
	 * @return the command script
	 */
	public final String getCommandScript() {
		return commandScript;
	}
	
	/**
	 * Gets the neighbor set.
	 * 
	 * @return the neighbor set
	 */
	public final Set<Address> getNeighborSet() {
		return neighborSet;
	}
	
	/**
	 * Gets the self.
	 * 
	 * @return the self
	 */
	public final Address getSelf() {
		return self;
	}
    
}
