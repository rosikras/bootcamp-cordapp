package bootcampCodingExample;

import com.google.common.collect.ImmutableList;
import net.corda.core.contracts.ContractState;
import net.corda.core.identity.AbstractParty;
import net.corda.core.identity.Party;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class HouseState implements ContractState {

    private String address;
    private Party owner;

    public HouseState(String address, Party owner) {
        this.address = address;
        this.owner = owner;
    }

    public String getAddress(){
        return this.address;
    }

    public Party getOwner(){
        return this.owner;
    }

    @NotNull
    @Override
    public List<AbstractParty> getParticipants() {
        // The owners that will be notified when the house is created, modified or destroyed
        return ImmutableList.of(owner);
    }

    public static void main(String [] args ){
        Party rosi = null;
        HouseState houseState = new HouseState("Donaufeld 54", rosi);
    }
}
