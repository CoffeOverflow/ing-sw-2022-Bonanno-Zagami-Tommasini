package it.polimi.ingsw.Model;

public interface Effect {

    void effect(Player player, Island island, CharacterCard card);
}

class Effect1 implements Effect{

    public void effect(Player player, Island island, CharacterCard card){//carta che ha 4 studenti e il giocatore ne sceglie uno da mettere su un'isola
        island.addStudents(card.chosenStudents.get().keySet().iterator().next(),1);
    }
}

class Effect2 implements Effect{

    //calcola maggioranza su isola scelta come se madre natura ci fosse finita sopra,
    public void effect(Player player, Island island, CharacterCard card){
        int max=0;
        int n=0;
        int position;
        for(int i=0; i<5; i++) {
            n=island.getStudentsOf(Color.values()[i]);
            if(n>max){
                max=n;
                position=i;
            }
        }
        //TODO capire come posso vedere div'è il professore del colore massimo
        //if(island.getTower())
    }
}
class Effect3 implements Effect{

    //puoi muovere madre natura fino a due passi in più rispetto alla tua carta assistente
    public void effect(Player player, Island island, CharacterCard card){

    }
}

class Effect4 implements Effect{

    //piazza una tessera divieto su isola scelta, la prima volta che madre natura ci passainfluenza non calcolata
    public void effect(Player player, Island island, CharacterCard card){
        island.setNoEntryCard(true);
    }
}

class Effect5 implements Effect{

    //durante il conteggio di un'influenza su un'isola le torri non vengono calcolate
    public void effect(Player player, Island island, CharacterCard card){

    }
}

class Effect6 implements Effect{

    //carta che ha 6 studenti, il giocatore ne può prendere al max 3 e scambiarli con 3 presenti nel suo ingresso
    public void effect(Player player, Island island, CharacterCard card){
        //TODO trovare un modo per passargli anche gli studenti da togliere dall'ingresso della scuola come parametro
    }
}
class Effect7 implements Effect{

    //due punti influenza addizionali nel calcolo dell'influenza
    public void effect(Player player, Island island, CharacterCard card){

    }
}

class Effect8 implements Effect{

    //scegli un colore di studente che in quel turno non cointribuisce al calcolo dell'influenza
    public void effect(Player player, Island island, CharacterCard card){

    }
}

class Effect9 implements Effect{

    //puoi scambiare fino a due studenti tra sala e ingresso
    public void effect(Player player, Island island, CharacterCard card){

    }
}

class Effect10 implements Effect{

    //carta che ha 4 studenti, il giocatore ne può scegliere uno da mettere nella sua sala
    public void effect(Player player, Island island, CharacterCard card){
        //int n=player.getStudentsOf(card.chosenStudents.get().keySet().iterator().next());
        //player.setStudents(card.chosenStudents.get().keySet().iterator().next(),n+1);
    }
}

class Effect11 implements Effect{

    //scegli un colore di studente, tutti (incluso te) rimetteranno tre studenti di quel colore presenti nella loro sala nel sacchetto
    public void effect(Player player, Island island, CharacterCard card){

    }
}

class Effect12 implements Effect{

    //durante questo turno prendi il controllo dei professori anche se nella tua sala hai lo stesso numero di studenti
    //del giocatore che li controlla in quel momento
    public void effect(Player player, Island island, CharacterCard card){

    }
}