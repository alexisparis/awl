/*
 * MessageLevel.java
 *
 * Created on 22 décembre 2007, 02:08
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.awl.message;

/**
 *
 * @author alexis
 */
public final class MessageLevel implements Comparable
{
    /** INFO */
    public static final MessageLevel INFO  = new MessageLevel(10);
    
    /** WARN */
    public static final MessageLevel WARN  = new MessageLevel(40);
    
    /** ERROR */
    public static final MessageLevel ERROR = new MessageLevel(90);
    
    /** FATAL */
    public static final MessageLevel FATAL = new MessageLevel(150);
    
    /** arrays defining the available level */
    public static final MessageLevel[] LEVELS = new MessageLevel[]{INFO, WARN, ERROR, FATAL};
    
    /** priority
     *	the more the priority is, the more important the level is
     */
    private int priority = -1;
    
    /** Creates a new instance of MessageLevel
     *	@param priority the priority of the level
     */
    private MessageLevel(int priority)
    {	
	this.priority = priority;
    }

    public boolean equals(Object obj)
    {
	boolean result = false;
	
	if ( obj instanceof MessageLevel )
	{
	    result = this.priority == ((MessageLevel)obj).priority;
	}
	
	return result;
    }

    public int hashCode()
    {
	return this.priority;
    }

    /**
     * Compares this object with the specified object for order.  Returns a
     * negative integer, zero, or a positive integer as this object is less
     * than, equal to, or greater than the specified object.
     * 
     * <p>The implementor must ensure <tt>sgn(x.compareTo(y)) ==
     * -sgn(y.compareTo(x))</tt> for all <tt>x</tt> and <tt>y</tt>.  (This
     * implies that <tt>x.compareTo(y)</tt> must throw an exception iff
     * <tt>y.compareTo(x)</tt> throws an exception.)
     * 
     * <p>The implementor must also ensure that the relation is transitive:
     * <tt>(x.compareTo(y)&gt;0 &amp;&amp; y.compareTo(z)&gt;0)</tt> implies
     * <tt>x.compareTo(z)&gt;0</tt>.
     * 
     * <p>Finally, the implementor must ensure that <tt>x.compareTo(y)==0</tt>
     * implies that <tt>sgn(x.compareTo(z)) == sgn(y.compareTo(z))</tt>, for
     * all <tt>z</tt>.
     * 
     * <p>It is strongly recommended, but <i>not</i> strictly required that
     * <tt>(x.compareTo(y)==0) == (x.equals(y))</tt>.  Generally speaking, any
     * class that implements the <tt>Comparable</tt> interface and violates
     * this condition should clearly indicate this fact.  The recommended
     * language is "Note: this class has a natural ordering that is
     * inconsistent with equals."
     * 
     * <p>In the foregoing description, the notation
     * <tt>sgn(</tt><i>expression</i><tt>)</tt> designates the mathematical
     * <i>signum</i> function, which is defined to return one of <tt>-1</tt>,
     * <tt>0</tt>, or <tt>1</tt> according to whether the value of
     * <i>expression</i> is negative, zero or positive.
     * 
     * @param o the object to be compared.
     * @return a negative integer, zero, or a positive integer as this object
     * 		is less than, equal to, or greater than the specified object.
     * @throws ClassCastException if the specified object's type prevents it
     *         from being compared to this object.
     */
    public int compareTo(Object o)
    {
	int result = 0;
	
	if ( o instanceof MessageLevel )
	{
	    result = this.priority - ((MessageLevel)o).priority;
	}
	else
	{
	    result = 1;
	}
	
	
	return result;
    }
    
}
