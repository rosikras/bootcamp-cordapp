package bootcampCodingExample;

import com.google.common.collect.ImmutableList;
import net.corda.core.contracts.ContractState;
import net.corda.core.identity.AbstractParty;
import net.corda.core.identity.Party;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ContainerState implements ContractState {
    private int windth;
    private int height;
    private int depth;
    private String content;
    private Party owner;
    private Party carrier;

    public ContainerState(int windth, int height, int depth, String content, Party owner, Party carrier) {
        this.windth = windth;
        this.height = height;
        this.depth = depth;
        this.content = content;
        this.owner = owner;
        this.carrier = carrier;
    }

    public int getWindth() {
        return windth;
    }

    public int getHeight() {
        return height;
    }

    public int getDepth() {
        return depth;
    }

    public String getContent() {
        return content;
    }

    public Party getOwner() {
        return owner;
    }

    public Party getCarrier() {
        return carrier;
    }

    @NotNull
    @Override
    public List<AbstractParty> getParticipants() {
        return ImmutableList.of(owner, carrier);
    }

    public static void main (String [] args){

        Party bagImporters = null;
        Party bagCarrier = null;
        ContainerState containerState = new ContainerState(2, 4, 2, "bags", bagImporters, bagCarrier);
    }


}
