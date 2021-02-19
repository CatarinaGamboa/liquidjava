package regen.test.project;


public class QuickSort {
    /* This function takes last element as pivot, 
    places the pivot element at its correct 
    position in sorted array, and places all 
    smaller (smaller than pivot) to left of 
    pivot and all greater elements to right 
    of pivot
     */
    int partition(int[] arr, @repair.regen.specification.Refinement("_ >= 0 && _ < length(arr)")
    int low, @repair.regen.specification.Refinement("_ >= 0 && _ < length(arr)")
    int high) {
        int pivot = arr[high];
        @repair.regen.specification.Refinement("_ >= -1 && _ < (length(arr)-1)")
        int i = low - 1;// index of smaller element

        for (int j = low; j < high; j++) {
            // If current element is smaller than the pivot
            if ((arr[j]) < pivot) {
                i++;
                // swap arr[i] and arr[j]
                int temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
            }
        }
        // swap arr[i+1] and arr[high] (or pivot)
        int temp = arr[(i + 1)];
        arr[(i + 1)] = arr[high];
        arr[high] = temp;
        return i + 1;
    }

    /* The main function that implements QuickSort() 
    arr[] --> Array to be sorted, 
    low  --> Starting index, 
    high  --> Ending index
     */
    void sort(@repair.regen.specification.Refinement("length(arr) > 0")
    int[] arr, @repair.regen.specification.Refinement("_ >= 0 && _ < length(arr)")
    int low, @repair.regen.specification.Refinement("_ >= 0 && _ < length(arr)")
    int high) {
        if (low < high) {
            /* pi is partitioning index, arr[pi] is  
            now at right place
             */
            int pi = partition(arr, low, high);
            // Recursively sort elements before
            // partition and after partition
            sort(arr, low, (pi - 1));
            sort(arr, (pi + 1), high);
        }
    }

    // Driver program
    public static void main(java.lang.String[] args) {
        int[] arr = new int[]{ 10, 7, 8, 9, 1, 5 };
        int n = arr.length;
        regen.test.project.QuickSort ob = new regen.test.project.QuickSort();
        ob.sort(arr, 0, (n - 1));
        java.lang.System.out.println("sorted array");
        regen.test.project.QuickSort.printArray(arr);
    }

    /* A utility function to print array of size n */
    static void printArray(int[] arr) {
        int n = arr.length;
        for (int i = 0; i < n; ++i)
            java.lang.System.out.print(((arr[i]) + " "));

        java.lang.System.out.println();
    }
}

