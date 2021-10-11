package bootcamp;

import net.corda.core.contracts.CommandData;
import net.corda.core.contracts.CommandWithParties;
import net.corda.core.contracts.Contract;
import net.corda.core.contracts.ContractState;
import net.corda.core.transactions.LedgerTransaction;
import static net.corda.core.contracts.ContractsDSL.requireSingleCommand;    
import static net.corda.core.contracts.ContractsDSL.requireThat;

import java.security.PublicKey;
import java.util.List;

/* Our contract, governing how our state will evolve over time.
 * See src/main/java/examples/ArtContract.java for an example. */
public class TokenContract implements Contract {
    public static String ID = "bootcamp.TokenContract";

    public interface Commands extends CommandData {
        class Issue implements Commands { }
    }

    @Override
    public void verify(LedgerTransaction tx) throws IllegalArgumentException {
        CommandWithParties<Commands> command = requireSingleCommand(tx.getCommands(),Commands.class);

        if (tx.getCommand(0).getValue() instanceof Commands.Issue){
            // Checking the shape of the transaction.
            if(tx.getInputStates().size()!=0){
                throw new IllegalArgumentException("Issue transaction must have no inputs.");
            }
            if(tx.getOutputStates().size() != 1){
                throw new IllegalArgumentException("Issuance transaction must have one output.");
            }

            // Checking the transaction's contents.
            ContractState outputState = tx.getOutputStates().get(0);
            if(!(outputState instanceof TokenState)){
                new IllegalArgumentException("Issuance transfer output must be a TokenState.");
            }

            TokenState tokenStateOutput = (TokenState) outputState;

            if(tokenStateOutput.getAmount()<=0){
                new IllegalArgumentException("Issuance transfer output must have a positive amount.");
            }

            //  Checking the transaction's required signers.
            final List<PublicKey> requiredSigners = command.getSigners();
            if(!requiredSigners.contains(tokenStateOutput.getIssuer().getOwningKey())){
                new IllegalArgumentException("Issuance transfer must be signed by the issuer.");
            }

        }else{
            throw new IllegalArgumentException("Unrecognised command");
        }
    }

}
