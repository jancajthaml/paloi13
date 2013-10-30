package pal;

public class Matrix
{

   private double[][] A;
   private int n;

   public Matrix(int n)
   {
      this.n = n;
      A = new double[n][n];
   }

   public void set( int i, int j, double s )
   { A[i][j] = s; }

   public void increment(int i, int j)
   { A[i][j]++; }
   
   public int determinant()
   { return determinant(n); }

   //Determinant using Gaussian elimination
   public int determinant( int n )
   {
	   int[] piv = new int[n];
	   
	   for( int i = 0; i < n; i++ )
		   piv[i] = i;
	      
	   int sign		= 1;
	   double[] LUrowi	= null;
	   double[] LUcolj	= new double[n];

	   for( int j = 0; j < n; j++ )
	   {
		   for (int i = 0; i < n; i++)
			   LUcolj[i] = A[i][j];

		   for (int i = 0; i < n; i++)
		   {
			   LUrowi = A[i];

			   int kmax = Math.min(i,j);
			   double s = 0.0;

			   for (int k = 0; k < kmax; k++)
				   s += LUrowi[k]*LUcolj[k];
	    		  
			   LUrowi[j] = LUcolj[i] -= s;
		   }
	   
		   int p = j;
	         
		   for (int i = j+1; i < n; i++) if (Math.abs(LUcolj[i]) > Math.abs(LUcolj[p]))
			   p = i;
		   
		   if (p != j)
		   {
			   for (int k = 0; k < n; k++)
			   {
				   double t = A[p][k];
				   A[p][k] = A[j][k];
				   A[j][k] = t;
			   }
	        	 
			   int k	= piv[p];
			   piv[p]	= piv[j];
			   piv[j]	= k;
			   sign		= -sign;
		   }

		   if (j < n & A[j][j] != 0.0)
		   {
			   for (int i = j+1; i < n; i++)
				   A[i][j] /= A[j][j];
		   }
	   }

	   double d = sign;
		   
	   for( int j = 0; j < n; j++ )
		   d *= A[j][j];

	   return (int)Math.round(d);
   }

}