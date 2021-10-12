package bootcampCodingExample;

import co.paralleluniverse.fibers.Suspendable;
import net.corda.core.contracts.StateAndRef;
import net.corda.core.flows.FlowException;
import net.corda.core.flows.FlowLogic;
import net.corda.core.flows.FlowSession;
import net.corda.core.flows.InitiatedBy;
import net.corda.core.identity.CordaX500Name;
import net.corda.core.identity.Party;
import net.corda.core.node.NodeInfo;
import net.corda.core.node.ServiceHub;

import java.util.List;

/**
 * Once we Install this CordApp on the node then the node will add this TwoPartyFlowResponder to it's List
 * of responder flows. This Means that whenever the node receives a message from an Instance of TwoPartyFlow
 * it's gonna go to it's List of registered responders, it's going to check if it has a valid responder to TwoPartyFlow,
 * it's going to look through it's List, it's goig to find that TwoPartyFlowResponder is annotated to respond to
 * TwoPartyFlow and it's going to create an instanceof this flow to speak to TwoPartyFlow
 */
@InitiatedBy(TwoPartyFlow.class)
public class TwoPartyFlowResponder extends FlowLogic<Void> {
    private FlowSession counterpartyFlowSession;
    //Any responder flow must have a single Contructor which takes the FlowSession.
    // When your node receives a Message from someone running the TwoPartyFlow they're going to
    // start a FlowSession with you and int the process of creating that flow session they're going to create an
    // instance of this flow to respond.
    public TwoPartyFlowResponder(FlowSession counterpartyFlowSession){
        this.counterpartyFlowSession=counterpartyFlowSession;
    }

    @Suspendable
    public Void call() throws FlowException {

        //The ServiceHub is an Object that is available to the node inside flows
        // with getServiceHub() we get access to it.
        // The ServiceHub is a portal to all the various services the node offers. So inside the ServiceHub
        // we'll have a list of past transactions, of Stated we know about, of the network map etc.
        ServiceHub serviceHub = getServiceHub();

        // If you want to extract existing states from your node:
        List<StateAndRef<HouseState>> houseStates = serviceHub.getVaultService().queryBy(HouseState.class).getStates();

        //If you want to get the Identity of a Specific Party
        CordaX500Name alicesName = new CordaX500Name("Allice", "Manchester", "UK");
        NodeInfo alice = serviceHub.getNetworkMapCache().getNodeByLegalName(alicesName);

        //If I need my platform version
        int platformVersion = serviceHub.getMyInfo().getPlatformVersion();

        // We want to receive an Integer
        // We need to validate the Data we receive. We can do the validation with the unwrap function.
        int receivedInt = counterpartyFlowSession.receive(Integer.class).unwrap(it-> {
            if(it>3){
                throw new IllegalArgumentException("number too high.");
            }
            return it;
        });

        int receivedIntPlusOne = receivedInt +1;

        // we send back a response
        counterpartyFlowSession.send(receivedIntPlusOne);
        return null;
    }
}
