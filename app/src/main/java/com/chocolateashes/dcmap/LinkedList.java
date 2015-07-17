package com.chocolateashes.dcmap;

/**
 * Created by Ashwini on 4/20/15.
 */
public class LinkedList
{
        XY first;
        int sizecounter = 0;

        public LinkedList()
        {
            first = null;
        }

        public void add(double valuex, double valuey)
        {
            XY e = new XY(valuex, valuey);
            e.next = first;
            first = e;
            sizecounter ++;
        }

        public void remove(int index)
        {

        }

        public double getdataX (int index)
        {
            XY current = first;
            for(int i=0; i < sizecounter-index-1; i++)
            {
                current = current.next;
            }
            return current.dataX;
        }

        public double getdataY (int index)
        {
            XY current = first;
            for(int i=0; i < sizecounter-index-1; i++)
            {
                current = current.next;
            }
            return current.dataY;
        }

        public int size()
        {
            return sizecounter; //dummy
        }

}
