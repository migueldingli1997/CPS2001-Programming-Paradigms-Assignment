#include <stdlib.h>
#include <stdio.h>
#include <assert.h>

#define TRUE 1
#define FALSE 0
typedef int (*filter_fn)(int);
typedef int (*map_fn)(int);

typedef struct set_element {
   int value;
   struct set_element* next;
} set_element;

set_element* create(int elem);
void destroy(set_element* head);
int add(set_element* src, int elem);
int delete(set_element* src, int elem);
int contains(set_element* src, int elem);
set_element* set_union(set_element* first, set_element* second);
set_element* set_diff(set_element* first, set_element* second);
set_element* set_intersection(set_element* first, set_element* second);
void print_set(set_element* start);
int for_all(set_element* set, filter_fn filter);
int exists(set_element* set, filter_fn filter);
set_element* map(set_element* head, map_fn map);