package no.hvl.dat102.mengde.kjedet;

//********************************************************************
// Kjedet implementasjon av en mengde. 
//********************************************************************
import java.util.*;

import no.hvl.dat102.exception.EmptyCollectionException;
import no.hvl.dat102.mengde.adt.*;

public class KjedetMengde<T> implements MengdeADT<T> {
	private static Random rand = new Random();
	private int antall; // antall elementer i mengden
	private LinearNode<T> start;

	/**
	 * Oppretter en tom mengde.
	 */
	public KjedetMengde() {
		antall = 0;
		start = null;
	}//

	@Override
	public void leggTil(T element) {
		if (!(inneholder(element))) {
			LinearNode<T> node = new LinearNode<T>(element);
			node.setNeste(start);
			start = node;
			antall++;
		}
	}

	@Override
	public void leggTilAlle(MengdeADT<T> m2) {
		Iterator<T> teller = m2.oppramser();
		while (teller.hasNext()) {
			leggTil(teller.next());
		}
	}

	@Override
	public T fjernTilfeldig() {
		if (erTom())
			throw new EmptyCollectionException("mengde");

		LinearNode<T> forgjenger, aktuell;
		T resultat = null;

		int valg = rand.nextInt(antall) + 1;
		if (valg == 1) {
			resultat = start.getElement();
			start = start.getNeste();
		} else {
			forgjenger = start;
			for (int nr = 2; nr < valg; nr++) {
				forgjenger = forgjenger.getNeste();
			}
			aktuell = forgjenger.getNeste();
			resultat = aktuell.getElement();
			forgjenger.setNeste(aktuell.getNeste());
		}
		antall--;

		return resultat;

	}//

	@Override
	public T fjern(T element) {

		if (erTom())
			throw new EmptyCollectionException("mengde");

		boolean funnet = false;
		LinearNode<T> forgjenger, aktuell;
		T resultat = null;
		
		if(start.getElement().equals(element)) {
			resultat = start.getElement();
			start = start.getNeste();
			antall--;
		} else {
			forgjenger = start;
			aktuell = start.getNeste();
			for(int pos = 1; pos < antall && !funnet; pos++) {
				if(aktuell.getElement().equals(element)) {
					resultat = aktuell.getElement();
					forgjenger.setNeste(aktuell.getNeste());
					antall--;
					funnet = true;
				} else {
					forgjenger = aktuell;
					aktuell = aktuell.getNeste();
				}
			}
		}
		
		return resultat;
	}//

	@Override
	public boolean inneholder(T element) {
		boolean funnet = false;
		LinearNode<T> aktuell = start;
		for (int soek = 0; soek < antall && !funnet; soek++) {
			if (aktuell.getElement().equals(element)) {
				funnet = true;
			} else {
				aktuell = aktuell.getNeste();
			}
		}
		return funnet;
	}

	@Override
	public boolean equals(Object m2) {
		boolean likeMengder = true;
		T element = null;
		
		MengdeADT<T> mengde2 = (KjedetMengde<T>)m2;
		
        if (this.antall() == mengde2.antall()) {
            Iterator<T> teller = mengde2.oppramser();
            while (teller.hasNext() && likeMengder) {
            	element = teller.next();
                if (this.inneholder(element) == false) {
                    likeMengder = false;
                }
            }
 
        } else {
            likeMengder = false;
        }
		
		return likeMengder;
	}

	@Override
	public boolean erTom() {
		return antall == 0;
	}

	@Override
	public int antall() {
		return antall;
	}

	@Override
	public MengdeADT<T> union(MengdeADT<T> m2) {
		MengdeADT<T> begge = new KjedetMengde<T>();
		LinearNode<T> aktuell = start;
		T element = null;

		//Adding m1(this)-set unconditionally
		while(aktuell != null) {
			 ((KjedetMengde<T>) begge).settInn(aktuell.getElement());
			 aktuell = aktuell.getNeste();
		}
		
		//Creating an Iterator in order to use hasNext and next
		Iterator<T> teller = m2.oppramser();
		
		//If this set doesn't already contain the element, add it
		while(teller.hasNext()) {
			element = teller.next();
			if(!this.inneholder(element)) {
				((KjedetMengde<T>) begge).settInn(element);
			}
		}
		
		return begge;
	}//

	@Override
	public MengdeADT<T> snitt(MengdeADT<T> m2) {
		MengdeADT<T> snittM = new KjedetMengde<T>();
		T element;
		
		Iterator<T> teller = m2.oppramser();
		
		while(teller.hasNext()) {
			element = teller.next();
			if (this.inneholder(element))	
				((KjedetMengde<T>) snittM).settInn(element);
		}
		
		return snittM;
	}

	@Override
	public MengdeADT<T> differens(MengdeADT<T> m2) {
		MengdeADT<T> differensM = new KjedetMengde<T>();
		T element;
		
		Iterator<T> teller = this.oppramser();
		
		while(teller.hasNext()) {
			element = teller.next();
			if(!m2.inneholder(element)) {
				((KjedetMengde<T>) differensM).settInn(element);
			}
		}

		return differensM;
	}

	@Override
	public boolean undermengde(MengdeADT<T> m2) {
		boolean erUnderMengde = true;
		Iterator<T> teller = m2.oppramser();
		
		while(teller.hasNext()) {
			T element = teller.next();
			if(!this.inneholder(element)) {
				erUnderMengde = false;
			}
		}
		
		return erUnderMengde;
	}

	@Override
	public Iterator<T> oppramser() {
		return new KjedetIterator<T>(start);
	}

	private void settInn(T element) {
		LinearNode<T> nyNode = new LinearNode<T>(element);
		nyNode.setNeste(start);
		start = nyNode;
		antall++;
	}
	
	/******************************************
	 * Returns a string that represents the set
	 ******************************************/
	public String toString() {
		
		String resultat = "";
		LinearNode<T> aktuell = start;
		while(aktuell!=null) {
			resultat += aktuell.getElement().toString() + "\t";
			aktuell = aktuell.getNeste();
		}
		return resultat;
	}

}// class
