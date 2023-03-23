# Here will be documented the design process for separation logic-related changes to liquid java

There are three major parts to add. The annotation language - the way to encode separation logic primitives in lanuguage used inside LJ annotations, java annotations - the way to annotate java code with Liquid Java, and finally, the examples of SL usage in LJ.

There is a huge problem regarding the mutablility of data. For example:

```
x = [1, 2, 3]
y = [4, 5, 6]
z = [7, 8, 9]

xyz = (x ++ y) ++ z
```

Now `xyz == [1, 2, 3, 4, 5, 6, 7, 8, 9]`, but these elements are not copied and now it is possible to mutate them from different places. It is bad on it's own, but one can forbid mutations. The worst problem is that if one will concatenate `y` to `xyz` then the loop will form and there is no way to forbid such behaviour with separation logic. 

It is seems possible to solve it by adding some kind of destructive predicate to concatination.

For example that if `xy = x ++ y` then `x` and `y` are no loginer lists. 

# Anotatations themselves

As there are now two implications and two conjunctions, it is important to distinct between them. 

# Anotatation language

The main challange is to distinct between values and pointer as they are treated different in SL. Maybe it is wise to introduce a simple type system to check this things in advance, before actully verifying anything.

# Example proofs

The part where the goal is to show how cool it is to have separation logic support in LJ. 

## Linked list

logic things:

```
data List = (head : Loc)
data Node = (data : Data, next : Loc)

isList(list : List) = 
    let node = List.head
    in noLoops(node)
  where
    noLoops (node : Loc) = 
           (exists (v : Node) . node -> v * noLoops(v.next))
        || (node == sep.nil)

```
Let see how it truns out for some concrete list

```
todo

```

Java things

```java
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

    //HeapRefinement is connected with context via separating conjunction instead of usual conjunction

    //We can mark some objests as invalid lists, 
    //but now we need to check if 'this' is a valid list itself.
    @HeapRefinement("isList(_) * !isList(another)") // <- * or &&?
    //                                  +- tells that 'another' is separate from 'this'.
    //                                  |  but does not tell that isList(this)
    //                                  v  so isList guarantees that there are no loops
    public void concat(@HeapRefinement("isList(another)") 
                       LinedList another){
        // this -> this_value * isList(another)
        if (this.head == null){
            this.head = another.head;
        }else{
            var curNode = this.head;
            while(curNode.next != null){
                curNode = curNode.next;
            }

            curNode.next = another.head;
        }
        // resulting refinement after invokation: isList(this) * !isList(another)
    }

    @HeapRefinement("isList(lhs) * !isList(rhs)")
    public static void concat(@HeapRefinement("isList(lhs)")
                             LinkedList lhs, 
                              @HeapRefinement("isList(rhs)")
                             LinkedList rhs){
        //...
    }

    //Now method will call concat on this and anther and everybody will be fine


    //Question: Should there be separate annotations for precondition and postcondition for heap? 
    //Because now it is unclear if attaching heapRefinement to context via separating conjunction and Refinement with usual conjunction is good enough.
    //Answer: ???


    @HeadPrefinement("isList(_)")
    @HeapPreconditionRefinement("isList(this)") 
    // ^ should make possible to use 'this' in precondition
    public void remove(Object data){
        //...
    }

    static class Node{
        Object data;
        Node next;

        
        @HeapRefinement("(_ -> node_value) * (node_value.next -> next)")
        //Separates this from next, ensuring loop absence.
        public static Node makeNode(Object data,              
                                    //v ensures that next is not null
                                    @HeapRefinement("next -> -")
                                    Node next){
            // next -> -
            Node node = new Node(data, next);
            // next -> - * node -> node_value
            return node;
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

### How does it help?

```java
var s = LinkedList.singleton(null);
s.concat(s); // Compile error!

```

```java
var s1 = LinkedList.singleton(null);
var s2 = LinkedList.singleton(null);
s1.concat(s2); 
s1.concat(s2); //Compile error!
```

## Binary tree

```java
class BinaryTreeNode{
    Object value;
    BinaryTree left;
    BinaryTree right;
    
    @HeapRefinement("isTree(_)")
    static BinaryTree makeTreeNode(
                        @HeapRefinement("isTree(left)")
                        BinaryTreeNode left, 
                        @HeapRefinement("isTree(right)")
                        BinaryTreeNode right, 
                        Object value){
        Tree t = new BinaryTreeNode();
        t.value = value;
        t.left = left;
        t.right = right;
        return t;
    }}
```


### How does it help?


```java
BinaryTreeNode left;  // = ...
BinaryTreeNode right; // = ...

BinaryTreeNode t = BinaryTreeNode.makeTreeNode(left, left, null); //Compile error!

```

```java
BinaryTreeNode left;  // = ...
BinaryTreeNode right; // = ...

BinaryTreeNode t = BinaryTreeNode.makeTreeNode(t, right, null); //Compile error!

```


## Unbounded buffer