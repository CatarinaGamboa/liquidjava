# Here will be documented the design process for separation logic-related changes to liquid java

There are three major parts to add. The annotation language - the way to encode separation logic primitives in lanuguage used inside LJ annotations, java annotations - the way to annotate java code with Liquid Java, and finally, the examples of SL usage in LJ.

# Anotatations themselves

As there are now two implications and two conjunctions, it is important to distinct between them. 

# Anotatation language

The main challange is to distinct between values and pointer as they are treated different in SL. Maybe it is wise to introduce a simple type system to check this things in advance, before actully verifying anything.

# Example proofs

The part where the goal is to show how cool it is to have separation logic support in LJ. 

## Linked list

```java

//Question: Will it help anybody to know this?
@StructuralPredicate("isList(list)"
                    , "isList(list) => (list -> list_value) && (list_value.head == sep.nil)"
                    , "isList(list) => (list -> list_value) * isList(tail) && (list_value.head == tail)"
)
class LinkedList{
    Node head;

    private LinedList(){}

    @Refinement("isList(_)")
    static LinkedList empty(){
        var list = new LinkedList();
        // list -> list_value
        list.head = null;
        // (list -> list_value) && (list_value.head == sep.nil`)
        return list;
    }
    @Refinement("isList(_)")
    static LinkedList singleton(Object data){
        var list = new LinkedList();
        // list -> list_value
        var headNode = Node.makeNode(data);
        // (head_node.next == sep.nil) * (headNode -> node_value) * (list -> list_value) 
        list.head = headNode;
        //    (node_value.next == sep.nil) 
        // *  (headNode -> node_value) 
        // *  (list -> list_value) 
        // && (list.head == headNode)
        return list;
    }

    //Question: should it mean that "isList(list) => "
    //                                     (list -> list_value) && (list_value.head == sep.nil)
    //                                  || (  (list -> list_value) 
    //                                     *  (headNode -> node_value) 
    //                                     *  (head_value.next == sep.nil) 
    //                                     && (list_value.head == headNode))
    // ?
    // One one hand it will mean that there are many variables in context corresponding to head_node, list, list_value, node_value and so on, on the other hand then it does not make sense to introduce isList at all.
    // Answer: Probably no, as there are more ways to obtain a list.

    //HeapRefinement is connected with context via separating conjunction instead of usual conjunction

    @HeapRefinement("isList(_)")
    //                                  +- tells that 'another' is separate from 'this'.
    //                                  v  so isList guarantees that there are no loops
    public void concat(@HeapRefinement("isList(another)") 
                       LinedList another){
        if (this.head == null){
            this.head = another.head;
        }else{
            var curNode = this.head;
            while(curNode.next != null){
                curNode = curNode.next;
            }

            curNode.next = another.head;
        }
        // resulting refinement after invokation: isList(this) * isList(another)
    }


    //Question: Should there be separate annotations for precondition and postcondition for heap? Because now it is unclear if attaching heapRefinement to context via separating conjunction and Refinement with usual conjunction is OK.
    //Answer: ???

    //Question: Should there be an "x reachable from y" predicate for pointers?
    //Answer: Probably no, as there is no unviersal procedure to determine that without running the program


    @HeadPrefinement("isList(_)")
    public void remove(Object data){
        //...
    }

    static class Node{
        Object data;
        Node next;

        
        @HeapRefinement("(_ -> node_value) * (node_value.next -> next)")
        //Separates this from next, ensuring loop anbsence.
        public static Node makeNode(Object data,              
                                    @HeapRefinement("next -> -")
                                    Node next){
            return new Node(data, next);
        }

        @HeapRefinement("(_ -> node_value) * (node_value.next == sep.nil)")
        public static Node makeNode(Object data){
            return new Node(data, null);
        }

        private Node(Object data, Node next){
            this.data = data;
            this.next = next;
        }
    }
}

```

## Binary tree

## Unbounded buffer