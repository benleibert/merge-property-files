package org.vr.utils;

import java.util.HashMap;

/*
    The advantage of this class over http://textmechanic.com/text-tools/basic-text-tools/sort-text-lines/
    is that it ensures consist whitespace between the key, value, and equals sign - and thus provides
    a consistent alphabetical sort order.
*/
public class SortPropertyFile
{

    public static void main(String[] args)
    {
        CombinePropertyFiles.loadPropertyFile("");
    }
}
