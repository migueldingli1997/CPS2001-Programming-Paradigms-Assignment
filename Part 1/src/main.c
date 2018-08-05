#include <stdio.h>
#include "sets.h"

int fltTest1(int elem) {
    return (elem%2)==0;
}
int fltTest2(int elem) {
    return (elem%2)==1;
}
int mapTest1(int elem) {
    return elem/2;
}
int mapTest2(int elem) {
    return elem*2;
}

int main() {

    set_element* set = create(0);
    printf("Step 0  "); print_set(set);
    printf("Contains test (OneItem:0)     : %s\n", contains(set,0)?"Success":"Failed");
    
    add(set, 1); //Add item test
    printf("Step 1  "); print_set(set);
    printf("Contains test (FirstItem:0)   : %s\n", contains(set,0)?"Success":"Failed");
    printf("Contains test (LastItem:1)    : %s\n", contains(set,1)?"Success":"Failed");
    printf("Contains test (InvalidItem:2) : %s\n", contains(set,2)?"Failed":"Success");
    
    add(set, 2); //Add more items test
    printf("Step 2  "); print_set(set);
    
    add(set, 2); //Duplicate test
    printf("Step 3  "); print_set(set);
    
    add(set, 3); //Add more items test
    printf("Step 4  "); print_set(set);
    
    delete(set, 0); //first item deletion test
    printf("Step 5  "); print_set(set);
    
    delete(set, 2); //middle item deletion test
    printf("Step 6  "); print_set(set);
    
    delete(set, 3); //last item deletion test
    printf("Step 7  "); print_set(set);
    
    delete(set, 4); //invalid deletion test
    printf("Step 8  "); print_set(set);
    
    delete(set, 1); //one-item deletion test
    //printf("9  "); print_set(set); //print after destroy (problematic)
    //AT THIS POINT, SET HAS BEEN DESTROYED BY DELETE

    ////////////////////////////////////////////////////////////////////////////////////

    set = create(0);
    destroy(set); //destroy test

    ////////////////////////////////////////////////////////////////////////////////////

    set_element* s1 = create(0);
    add(s1, 2);
    add(s1, 3);
    set_element* s2 = create(1);
    add(s2, 3);
    add(s2, 4);
    set_element* s3 = create(7);
    add(s3, 8);
    add(s3, 9); 

    printf("s1: "); print_set(s1);
    printf("s2: "); print_set(s2);
    printf("s3: "); print_set(s3);

    set_element* temp;
    temp = set_union(s1,s2);
    printf("Union (s1,s2):        "); print_set(temp); destroy(temp);

    temp = set_intersection(s1,s2);
    printf("Intersection (s1,s2): "); print_set(temp); destroy(temp);

    temp = set_diff(s1,s2); 
    printf("Difference (s1,s2):   "); print_set(temp); destroy(temp);

    temp = set_union(s1,s3);
    printf("Union (s1,s3):        "); print_set(temp); destroy(temp);

    temp = set_intersection(s1,s3);
    printf("Intersection (s1,s3): "); print_set(temp); //destroy(temp); (TEMP IS NULL)

    temp = set_diff(s1,s3); 
    printf("Difference (s1,s3):   "); print_set(temp); destroy(temp);

    destroy(s1);
    destroy(s2);
    destroy(s3);

    ////////////////////////////////////////////////////////////////////////////////////

    set_element* s4 = create(0);
    add(s4, 2);
    add(s4, 4);
    set_element* s5 = create(0);
    add(s5, 2);
    add(s5, 3);
    set_element* s6 = create(1);
    add(s6, 3);
    add(s6, 5);

    printf("s4: "); print_set(s4);
    printf("s5: "); print_set(s5);
    printf("s6: "); print_set(s6);

    printf("FORALL even (s4) : %s\n", for_all(s4,fltTest1)?"Success(T)":"Failed(F)");
    printf("EXISTS even (s4) : %s\n", exists(s4,fltTest1)?"Success(T)":"Failed(F)");
    printf("EXISTS even (s5) : %s\n", exists(s5,fltTest1)?"Success(T)":"Failed(F)");
    printf("FORALL even (s5) : %s\n", for_all(s5,fltTest1)?"Failed(T)":"Success(F)");
    printf("FORALL odd  (s6) : %s\n", for_all(s6,fltTest2)?"Success(T)":"Failed(F)");
    printf("EXISTS even (s6) : %s\n", for_all(s6,fltTest1)?"Failed(T)":"Success(F)");

    destroy(s4);
    destroy(s5);

    ////////////////////////////////////////////////////////////////////////////////////

    set_element* s7  = map(s6,mapTest2);
    set_element* s8  = map(s7,mapTest1);
    set_element* s9  = map(s8,mapTest1);
    set_element* s10 = map(s9,mapTest1);
    set_element* s11 = map(s10,mapTest1);

    printf("s7  :: MAP *2 (s6)  : "); print_set(s7);
    printf("s8  :: MAP /2 (s7)  : "); print_set(s8);
    printf("s9  :: MAP /2 (s8)  : "); print_set(s9);
    printf("s10 :: MAP /2 (s9)  : "); print_set(s10);
    printf("s11 :: MAP /2 (s10) : "); print_set(s11);

    destroy(s6);
    destroy(s7);
    destroy(s8);
    destroy(s9);
    destroy(s10);
    destroy(s11);
}