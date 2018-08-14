//UT-EID= CV7999, bsk524

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RecursiveAction;

public class ConcurrentMerge extends RecursiveAction {
    private int[] A;
    private int[] B;
    private int[] C;
    private int[] splitA;
    private int[] splitB;
    private int countC;
    private int global_count;

    public ConcurrentMerge(int[] A, int[] B, int[] splitA, int[] splitB, int[] C, int count) {
        this.A = A;
        this.B = B;
        this.splitA = splitA;
        this.splitB = splitB;
        this.global_count = count;
        this.C = C;
        countC = 0;
    }

    /*
     * compute() should recursively call splitArrayB (aka binary search) to ensure that array B is divided
     * correctly. Once the array is divided correctly, it can go ahead and merge.
     * This is where you would use the invokeAll() method from ForkJoinPool
     */
    @Override
    protected void compute() {
        if(global_count != 0 || splitA.length == 1){
            splitArrayB();
        }
        else{
            List<ConcurrentMerge> subtasks = new ArrayList<>();
            ConcurrentMerge temp;
            for(int i = 0; i < splitA.length; i++){
                global_count++;
                temp = new ConcurrentMerge(A, B, splitA, splitB, C, global_count);
                subtasks.add(temp);
            }
            invokeAll(subtasks);
            MergeC();
        }
    }

    /*
     * This method should take in array B and enter in the
     * LAST index of each "subarray" into dividedB. For instance:
     *
     * dividedB[0] contains the last index of array B that thread0 would
     * process (so it would look at the range:= 0 to dividedB[0] inclusive)
     */
    private void splitArrayB() {
        int count = 0;
        if(global_count == 1){
            int highA = A[splitA[global_count - 1]];
            for(int i = 0; i < B.length; i++){
                //if B in range, add to split B
                if(B[i] < highA){
                    count++;
                }
                else{
                    //if this is last B then split here
                    splitB[global_count - 1] = i;
                    break;
                }
            }
            if(count == 0){
                //if no B is added, -1
                splitB[global_count - 1] = -1;
                return;
            }
            splitB[global_count - 1] = count - 1;
        }
        else{
            int lowA = A[splitA[global_count - 2]];
            int highA = A[splitA[global_count - 1]];
            int i = 0;
            //while loop to find where lowB is
            while(B[i] < lowA){
                i++;
                if(i == B.length){
                    break;
                }
            }
            for(i = i; i < B.length; i++){
                //if B in range, add to split B
                if(B[i] < highA){
                    count++;
                }
                else{
                    break;
                }
            }
            splitB[global_count - 1] = i - 1;
            if(count == 0){
                splitB[global_count - 1] = -1;
            }
            if(global_count == A.length){
                splitB[global_count - 1] = B.length - 1;
                if(splitB[0] != - 1 && splitB[global_count - 2] == - 1 || splitB[global_count - 2] == B.length - 1){
                    splitB[global_count - 1] = -1;
                }
            }
        }
    }

    /*
     * Merge A and B into C for this thread
     */
    private void mergeAB(int aMin, int aMax, int bMin, int bMax) {
        int countA = aMin;
        int countB = bMin;
        while(countC < C.length){
            if(A[countA] < B[countB] && countA <= aMax){
                //add A to C
                C[countC] = A[countA];
                countA++;
                if(countA > aMax){
                    countC++;
                    break;
                }
            }
            else if(A[countA] >= B[countB] && countB <= bMax){
                //add B to C
                C[countC] = B[countB];
                countB++;
                if(countB > bMax){
                    countC++;
                    break;
                }
            }
            countC++;
        }
        while(countA <= aMax){
            if(countC >= C.length){
                return;
            }
            C[countC] = A[countA];
            countA++;
            countC++;
        }
        while(countB <= bMax){
            if(countC >= C.length){
                return;
            }
            C[countC] = B[countB];
            countC++;
            countB++;
        }
    }

    private void MergeC(){
        int maxA;
        int maxB;
        int count = 0;  //index number
        int countA = 0; //A index
        int countB = 0; //B index
        while(count < A.length){
            maxA = splitA[count];
            maxB = splitB[count];
            if(maxB == -1){
                for(countA = countA; countA <= maxA; countA++){
                    C[countC] = A[countA];
                    countC++;
                }
            }
            else {
                mergeAB(countA, maxA, countB, maxB);
                countA = maxA + 1;
                countB = maxB + 1;
            }
            count++;
        }
    }
}