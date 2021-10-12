package bootcampCodingExample;

import com.google.common.collect.ImmutableList;
import net.corda.core.contracts.ContractState;
import net.corda.core.contracts.StateAndRef;
import net.corda.core.identity.Party;
import net.corda.core.transactions.TransactionBuilder;

import java.security.PublicKey;
import java.util.List;

public class ScratchPad {

    public static void main (String args []){
        StateAndRef<ContractState> inputState = null;
        ContractState outputState = new HouseState("Donaufeld 54", null);
        PublicKey requiredSigner = ((HouseState)outputState).getOwner().getOwningKey();
        List<PublicKey> requiredSigners = ImmutableList.of(requiredSigner);
        Party notary = null;

        TransactionBuilder builder = new TransactionBuilder();
//        builder.addInputState(inputState);
//        builder.addOutputState();
//        builder.addCommand();

        // When you build a Transaction you need to pass a Notary as well.
        builder.setNotary(notary);

        //Input state Reference. This is already on the ledger so we don't need to specify a contract.
        builder.addInputState(inputState)
                // here we need to pass the fully qualified name of the contract as a string because this is a
                // newly created state.
                // We shouldn't pass the class because this would create a trust issue where everyone would use the
                // Version of the contract provided by the Builder and what happens id that's just a fake
                // DummyContract that is invalid?
                // So instead of loading the contract in the builder we will just pass the name of the contract to use
               // and each party on their own machine will load that from the classpath and will use that so
                // they don't have trust the builder to provide a valid contract
                .addOutputState(outputState,"bootcampCodingExample.HouseContract")
                .addCommand(new HouseContract.Register(), requiredSigners);

        // Once we verify the builder we can sign it and it becomes a fully signed transaction
        //builder.verify();
    }
}
