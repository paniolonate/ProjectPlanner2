package com.example.nate.projectplanner;


/***************************************************************************************
 * Reference to ICS 211 with Scott Robertson and TA Alyssa Higuchi
 * FILE: SLLNode.java
 *
 * DESCRIPTION: This file contains the SLLNode class
 *
 ***************************************************************************************/

public class SLLNode<E> {
    private E data;
    private SLLNode<E> next;

    /********************************************************************
     *
     * Method: SLLNode
     *
     * Description: Costructs an SLLNode object
     *
     * param  newData  The data which the SLLNode should contain
     *
     * return None
     *
     ********************************************************************/

    public SLLNode(E newData) {
        this.data = newData;
        this.next = null;
    }

    /********************************************************************
     *
     * Method: setData
     *
     * Description: Sets the data of this SLLNode to the given data
     *
     * param  newData  The data which the SLLNode should contain
     *
     * return None
     *
     ********************************************************************/

    public void setData(E newData) {
        this.data = newData;
    }

    /********************************************************************
     *
     * Method: setNext
     *
     * Description: Sets the next field of this SLLNode to the given SLLNode
     *
     * param  newNext  The SLLNode that next should point to
     *
     * return None
     *
     ********************************************************************/

    public void setNext(SLLNode newNext) {
        this.next = newNext;
    }


    /********************************************************************
     *
     * Method: getData
     *
     * Description: Gets the data of this SLLNode
     *
     *
     *
     * return this.data  The data that this SLLNode contains
     *
     ********************************************************************/

    public E getData() {
        return this.data;
    }

    /********************************************************************
     *
     * Method: getNext
     *
     * Description: Gets the SLLNode in the next field of this SLLNode
     *
     * return this.next  The SLLNode that this node points to
     *
     ********************************************************************/

    public SLLNode getNext() {
        return this.next;
    }
}
