package bootcampCodingExample;

import net.corda.core.contracts.Command;
import net.corda.core.contracts.CommandData;
import net.corda.core.contracts.Contract;
import net.corda.core.contracts.ContractState;
import net.corda.core.identity.Party;
import net.corda.core.transactions.LedgerTransaction;
import org.jetbrains.annotations.NotNull;

import java.security.PublicKey;
import java.util.List;

public class HouseContract implements Contract {

    @Override
    public void verify(@NotNull LedgerTransaction tx) throws IllegalArgumentException {
        // This Contract will not allow any transactions involving HouseState
        //throw new IllegalArgumentException("Acceps nothing!");

        if (tx.getCommands().size() != 1) {
            throw new IllegalArgumentException("Transaction must have one command.");
        }

        Command command = tx.getCommand(0);
        List<PublicKey> requiredSigners = command.getSigners();
        CommandData commandType = command.getValue();

        if (commandType instanceof Register) {
            //"Shape" Constraints:
            // constraints on the number of inputs, outputs, commands and their types as well
            if(tx.getInputStates().size()!=0){
                // When we register a house we don't expect a transaction to be on the ledger.
                // We are not updating or consuming anything.
                //This is the first time this house will be saved on the ledger that's why
                // the we expect the number of inputs of type HouseState to be 0
                throw new IllegalArgumentException("Registration Transaction must have no inputs.");
            }

            if(tx.getOutputStates().size()!=1){
                throw new IllegalArgumentException("Registration Transaction must have one output.");
            }

            // Content Constraints
            // Inspecting the commands, inputs and outputs and saying do they have the right values,
            // are some things expected to be constant, are things expected to be changing is that correct
            ContractState outputState = tx.getOutput(0);

            if(!(outputState instanceof HouseState)){
                throw new IllegalArgumentException("Output must be a HouseState.");
            }

            HouseState houseState = (HouseState) outputState;

            if(houseState.getAddress().length()<3){
                throw new IllegalArgumentException("Address must be longer than 3 characters.");
            }
            if(houseState.getOwner().getName().getCountry().equals("Brazil")){
                throw new IllegalArgumentException("Not allowed to register for Brazilian owners.");
            }

            //Required signer constraints
            Party owner = houseState.getOwner();
            PublicKey ownersKey = owner.getOwningKey();
            if(!requiredSigners.contains(ownersKey)){
                throw new IllegalArgumentException("Owner of house must sign registration.");
            }

        } else if (commandType instanceof Transfer) {

            // "Shape" constraints
            if(tx.getInputStates().size()!=1){
                throw new IllegalArgumentException("Must have one input.");
            }
            if(tx.getOutputStates().size()!=1){
                throw new IllegalArgumentException("Must have one output.");
            }

            //Contents constraints
            ContractState input = tx.getInput(0);
            ContractState output = tx.getOutput(0);

            if(!(input instanceof HouseState)){
                throw new IllegalArgumentException("Input must be a HouseState.");
            }
            if(!(output instanceof HouseState)){
                throw new IllegalArgumentException("Output must be a HouseState.");
            }

           HouseState inputHouse = (HouseState) input;
           HouseState outputHouse = (HouseState) output;

           if(!(inputHouse.getAddress().equals(outputHouse.getAddress()))){
               throw new IllegalArgumentException("In a transfer the address can't change.");
           }
           if(inputHouse.getOwner().equals(outputHouse.getOwner())){
               throw new IllegalArgumentException("In a transfer the owner must change.");
           }

           // Signer constraint
           Party inputOwner = inputHouse.getOwner();
           Party outputOwner = outputHouse.getOwner();

           if(!requiredSigners.contains(inputOwner.getOwningKey())){
               throw new IllegalArgumentException("Current owner must sign transfer.");
           }
            if(!requiredSigners.contains(outputOwner.getOwningKey())){
                throw new IllegalArgumentException("New owner must sign transfer.");
            }

        } else {
            throw new IllegalArgumentException("Command type not recognised");
        }

    }

    public static class Register implements CommandData {
    }

    public static class Transfer implements CommandData {
    }
}
