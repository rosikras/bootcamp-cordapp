package bootcampCodingExample;

import co.paralleluniverse.fibers.Suspendable;
import net.corda.core.flows.*;

/**
 * extends FlowLogic<Integer> - Flowlogic takes the return type of the flow. For the example it will be Integer
 *
 * @InitiatingFlow - This means this is a low that you can start on the node directly. The Initiating flow
 * isn't started on response to other flows but it is started just by the platform or by a service within the node
 *
 * @InitiatedBy() - this means this Flow is started in response to other flows
 *
 * @StartableByRPC - this means that the node owner when it's logged into the node VAR PC can start it directly. This
 * is a flow that the node operator can start.
 */

@InitiatingFlow
@StartableByRPC
public class VerySimpleFlow extends FlowLogic<Integer> {

    // We need to annotate this Method as @Suspendable,
    // Under the hood Corda uses a library called quasar to allow flows to be paused
    // and checkpoints at any point in time. To allow this the bytecode rewriting that allows to be
    // checkpoints and suspend at any point in time every call method must be annotated as @Suspendable
    @Suspendable
    // @Override - this is not needed
    public Integer call() throws FlowException {
        int a = returnOne();
        int b=2;
        return a+b;
    }


    public Integer returnOne(){
        return 1;
    }
}
