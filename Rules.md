## Rules for Liquid Type Checking in Java



**Constant**

-------------------------------

​					**Γ** **|-**  c : type(c) 			type = {int,...}

**Variable**

​					x : T in   **Γ** 

-------------------------------

​					**Γ** **|-**  x :  T



**Declaration**

**Γ** **|-** x : T1	      **Γ** **|-** e1: T			T **<:** T1

-------------------------------

​					**Γ** **|-**  U x = e1 :  T			U= {int,...}



**Assignment**

**Γ** **|-** x : T1	      **Γ** **|-** e1: T			T **<:** T1

-------------------------------

​					**Γ** **|-**  x = e1 :  T



**Arithmetic Operations**

**Γ** **|-** e1 : T1	      **Γ** **|-** e2: T2		**Γ** , e1:T1, e2:T2 **|-** (e1 op e2) : T

-------------------------------

​					    **Γ** **|-**  e1 op e2 :  T			op={+,-,*,/, %}





**Γ** **|-** e1 : T1	      **Γ** **|-** e2: T2		**Γ** e1:T1, e2:T2  **|-**  e1 op e2 : boolean

-------------------------------

​					             **Γ** **|-**  e1 op e2 :  boolean			op={||, &&}





**If** 

**Γ** **|-** e: boolean	      **Γ** , e**|-** S *valid*	

-------------------------------

​					**Γ** **|-**  if (e) S  *valid*



**If-Else**

**Γ** **|-** e: boolean	      **Γ** , e**|-** S1 *valid*        **Γ** , !e**|-** S2 *valid*	

-------------------------------

​					**Γ** **|-**  if (e) S1 else S2  *valid*



Implementação Ifs - passamos a expressao para os metadados dos filhos?



**While**

**Γ** **|-** e: boolean	      **Γ** , e**|-** S *valid*	

-------------------------------

​					**Γ** **|-**  while (e)  S  *valid*





e: expressions

S: statements

