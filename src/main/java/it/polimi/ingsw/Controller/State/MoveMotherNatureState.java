package it.polimi.ingsw.Controller.State;

import it.polimi.ingsw.Controller.Action;
import it.polimi.ingsw.Controller.GameController;
import it.polimi.ingsw.Model.GameModel;
import it.polimi.ingsw.Model.Tower;

import java.util.HashMap;
import java.util.Optional;

public class MoveMotherNatureState implements GameControllerState {
    @Override
    public void turnAction(GameController gc, Action action) {
        GameModel m=gc.getModel();
        int currentPlayer=m.getCurrentPlayer();
        if(action.getMotherNatureSteps()<=m.getCurrentCardPlayers().get(currentPlayer).getMothernatureSteps()){
            m.moveMotherNature(action.getMotherNatureSteps());
        }else{
            System.out.println("the number of steps chosen is higher than the one indicated by the assistant card");
        }
        //take control of the island:
        int key=0;
        HashMap<Integer, Integer> influences=new HashMap<>();
        Optional<Integer> conqueror=null;
        for(Integer p : m.getCurrentCardPlayers().keySet()){
            if(m.getTowerOnIsland(m.getMotherNaturePosition()).isPresent() &&
                    m.getTowerOnIsland(m.getMotherNaturePosition()).get().equals(m.getPlayerTower(p))){
                influences.put(p,m.getPlayerInfluence(p,m.getMotherNaturePosition())
                        + m.getIslandByPosition(m.getMotherNaturePosition()).getNumberOfTowers());
            }else{
                influences.put(p,m.getPlayerInfluence(p,m.getMotherNaturePosition()));
            }
            if(influences.get(p)>key){
                key=m.getPlayerInfluence(p,m.getMotherNaturePosition());
                conqueror=Optional.of(p);
            }
        }
        //check if the higher value of influence is unique
        if(conqueror.isPresent()){
            for(Integer p : m.getCurrentCardPlayers().keySet()){
                if(p!=conqueror.get() && influences.get(p)==influences.get(conqueror)){
                    conqueror=Optional.empty();
                }
            }
        }

        //if the value is unique, conquer the island
        if(conqueror.isPresent()){
            Optional<Tower> oldTower= m.getTowerOnIsland(m.getMotherNaturePosition());
            m.setTowerOnIsland(m.getMotherNaturePosition(),conqueror.get());
            if(oldTower.isPresent()){
                int oldNumberOfTower=m.getPlayerByTower(oldTower.get()).getNumberOfTower();
                m.getPlayerByTower(oldTower.get()).setNumberOfTower(oldNumberOfTower+1);
            }
            m.getPlayerByID(conqueror.get()).buildTower();

            checkMergeIsland(gc,m.getMotherNaturePosition(),
                    m.getPlayerTower(conqueror.get()));
        }

    }

    private void checkMergeIsland(GameController gc, int island, Tower tower){
        if(island==gc.getModel().getIslandSize()-1 && gc.getModel().getTowerOnIsland(island-1).equals(tower)){
            gc.getModel().mergeIslands(island-1,island);
            checkMergeIsland(gc, island-1,tower);
        }else if(island==gc.getModel().getIslandSize()-1 && gc.getModel().getTowerOnIsland(0).equals(tower) ){
            gc.getModel().mergeIslands(0,island);
            checkMergeIsland(gc, 0,tower);
        }else if(island==0 && gc.getModel().getTowerOnIsland(gc.getModel().getIslandSize()-1).equals(tower)){
            gc.getModel().mergeIslands(island,gc.getModel().getIslandSize()-1);
            checkMergeIsland(gc, island,tower);
        }else if((island-1)>=0 && gc.getModel().getTowerOnIsland(island-1).equals(tower)){
            gc.getModel().mergeIslands(island-1,island);
            checkMergeIsland(gc, island-1,tower);
        }else if((island+1)<gc.getModel().getIslandSize() && gc.getModel().getTowerOnIsland(island+1).equals(tower)){
            gc.getModel().mergeIslands(island,island+1);
            checkMergeIsland(gc, island,tower);
        }
    }
}
