package com.lipi.android.bazakonstrukcijskihsklopov.baza;

/*
 * 10/29/2010
 * Author: Bill Johnson - KATR Software Incorporated
 * Original Tutorial URL: http://www.katr.com/article_spinner01.php
 * http://www.katr.com 
 * 
 * If you like this example, please support the author by purchasing the "Halloween Eyes" live
 * wallpaper from the android market.  C'mon, it's only $0.99.
 * 
 */
public class MaterialiSpinnerVrstaSklopa {
	// Okay, full acknowledgment that public members are not a good idea, however
	// this is a Spinner demo not an exercise in java best practices.
	public int id = 0;
	public String name = "";

	// A simple constructor for populating our member variables for this tutorial.
	public MaterialiSpinnerVrstaSklopa( int _id, String _name)
	{
		id = _id;
		name = _name;
	}

	// The toString method is extremely important to making this class work with a Spinner
	// (or ListView) object because this is the method called when it is trying to represent
	// this object within the control.  If you do not have a toString() method, you WILL
	// get an exception.
	public String toString()
	{
		return( name );
	}	
}
