package no.hvl.dat102;

import java.util.Scanner;

public class KlientBalansering{
	public static void main(String[] args){

		System.out.println("Skriv inn filnavn");
		Scanner leser  = new Scanner(System.in);
		final String filnavn = leser.next();
		//Leser inn en tekst fra fil
		Balansering balansering = new Balansering();
		balansering.lesFraFil(filnavn);
	}//main

}//class
