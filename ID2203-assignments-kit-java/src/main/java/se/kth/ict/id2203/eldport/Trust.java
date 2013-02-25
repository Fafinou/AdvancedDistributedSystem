/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.ict.id2203.eldport;

import se.sics.kompics.Event;
import se.sics.kompics.address.Address;

/**
 *
 * @author ALEX & fingolfin
 */
public class Trust extends Event {
    private final Address leader;

    public Trust(Address leader) {
        this.leader = leader;
    }

    public Address getLeader() {
        return leader;
    }
    
    
    
}
