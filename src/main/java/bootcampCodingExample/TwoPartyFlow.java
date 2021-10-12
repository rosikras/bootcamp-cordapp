package bootcampCodingExample;

import co.paralleluniverse.fibers.Suspendable;
import net.corda.core.flows.*;
import net.corda.core.identity.Party;

/**
 * Here we've written a Flow which is an Initiating Flow that we can start via RPC. Once we install this
 * on our node we can always call the TwoPartyFlow, choose a counterparty, pass it in the constructor and saying
 * "I want to start a Flow with the counterparty and I want to send them a number and I want them to send me back this
 * number incremented by one"
 */
@InitiatingFlow
@StartableByRPC
public class TwoPartyFlow extends FlowLogic<Integer> {
    // this is some party on the network that we want to send some information to
    public Party counterparty;
    private Integer number;

    // We can now call this flow and speak to any counterparty we want
    public TwoPartyFlow(Party counterparty, Integer number) {
        this.counterparty = counterparty;
        this.number = number;
    }

    @Suspendable
    public Integer call() throws FlowException {
        //First we need to create a communication session with our party
        // The session is a channel of communication. This is how all communication between nodes happens.
        // So any time I want two nodes to communicate you send a request to your node you start flows on your node
        // and it's in the context of those flows you'll go any speak  to a counterparty node
        FlowSession session = initiateFlow(counterparty);
        // we sent an Integer
        session.send(number);

        int receivedIncrementedInteger = session.receive(Integer.class).unwrap(it-> it);

        return receivedIncrementedInteger;
    }
}
