#include <stdio.h>
#include "sets.h"

set_element* create(int elem) {

    set_element* s = malloc(sizeof(struct set_element));
    s -> value = elem;  //set first value in the set
    s -> next = NULL;   //NULL until another value added
    return s;
}

void destroy(set_element* head) {

    set_element* nextToFree;     //pointer to next element to be freed
    while(head != NULL) {
        nextToFree = head->next; //update to current element's next
        free(head);              //free current element
        head = nextToFree;       //move on to next element
    }
}

int add(set_element* src, int elem) {

    if(contains(src, elem)) {
        return FALSE; //set already contains value to be added
    }
    else {
        while(src->next != NULL)
            src = src -> next;      //Find end element
        src -> next = create(elem); //End element's next is now a new element
        return TRUE;
    }
}

int delete(set_element* src, int elem) {

    if(!contains(src, elem)) {
        return FALSE; //set does not contain elem
    }
    else if(src -> next == NULL) {
        destroy(src);
        return TRUE; //set only consists of elem
    }

    set_element* prevElem = NULL;
    while(src -> value != elem) { //Loop until value of src is elem
        prevElem = src; //Keep track of previous element
        src = src -> next;
    }
    
    //Get element after the element which has value elem
    set_element* nextElem = src -> next;

    //If element which has elem was the final element
    if(nextElem == NULL) {
        prevElem -> next = NULL;
        free(src); //free final element
    }
    else {
        src -> value = nextElem -> value;   //shift value
        src -> next  = nextElem -> next;    //shift pointer
        free(nextElem);                     //free extra element
    }

    return TRUE;
}

int contains(set_element* src, int elem) {

    //Check for elem in all elements except for last
    while(src -> next != NULL){
        if(src -> value == elem)
            return TRUE;
        src = src -> next;
    }

    return (src -> value == elem); //Check for elem in last element
}

set_element* set_union(set_element* first, set_element* second) {

    set_element* new = NULL;

    //Add all values from first set
    while(first != NULL){
        //If new has not been created, create it; otherwise add value
        if(new == NULL)
            new = create(first->value);
        else
            add(new, first->value);
        first = first->next;
    }

    //Add values from second set (duplication handled by 'add')
    while(second != NULL){
        add(new, second->value);
        second = second->next;
    }

    return new;
}

set_element* set_diff(set_element* first, set_element* second) {

    set_element* new = NULL;

    //Add values from first set which are not in second set
    while(first != NULL){
        //If second does not contain the value, add it
        if(!contains(second, first->value)){
            //If new has not been created, create it; otherwise add value
            if(new == NULL)
                new = create(first->value);
            else
                add(new, first->value);
        }
        first = first->next;
    }

    return new;
}

set_element* set_intersection(set_element* first, set_element* second) {

    set_element* new = NULL;

    //Add values from first set which are also in second set
    while(first != NULL){
        if(contains(second, first->value)){
            //If new has not been created, create it; otherwise add value
            if(new == NULL)
                new = create(first->value);
            else
                add(new, first->value);
        }
        first = first->next;
    }

    return new;
}

void print_set(set_element* start) {

    if(start == NULL){
        printf("NULL\n");
        return;
    }

    printf("{");
    while(start->next != NULL) {
        printf("%d,", start->value);
        start = start->next;
    }
    printf("%d}\n", start->value);
}

int for_all(set_element* set, filter_fn filter) {

    //False if at least value does not satisfy the filter
    while(set != NULL){
        if(!(*filter)(set->value))
            return FALSE;
        else
            set = set->next;
    }

    return TRUE;
}

int exists(set_element* set, filter_fn filter) {

    //True if at least one value satisfies the filter
    while(set != NULL){
        if((*filter)(set->value))
            return TRUE;
        else
            set = set->next;
    }

    return FALSE;
}

set_element* map(set_element* head, map_fn map) {

    set_element* new = NULL; //set for new values
    
    //Iterate over the elements of the set
    while(head != NULL){
        //If new is still empty, create it using mapped value
        if(new == NULL)
            new = create((*map)(head->value));
        else
            add(new, (*map)(head->value)); //'add' handles duplicates
        head = head->next;
    }
    
    return new; //return new set
}