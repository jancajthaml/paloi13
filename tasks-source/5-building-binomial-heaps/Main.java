package pal;

public class Main
{

    static  int                     M          =  0;
    static  int                     N          =  0;
    static  Fio                     in         =  null;
    static  int                     best_eval  =  0;
    static  Permutation             best       =  null;
    static  BinomialHeap            heap       =  new BinomialHeap();
    static  PermutationGenerator    generator  = null;

    //###############################################################################//
    
    public static void main( String ... args )
    {
        try
        {
            read_data             ( ) ;
            find_best_candidate   ( ) ;
            print_solution        ( ) ;
        }
        catch(Throwable t) { /*ignore*/  }
        finally            { in.close(); }
    }

    //###############################################################################//

    static void print_solution()
    {
        System.out.println ( best_eval ) ;
        System.out.println (   best    ) ;
    }

    static void find_best_candidate()
    {
        for( Permutation permutation : generator )
        {
        	             heap . recycle ( permutation ) ;
        	int diff  =  heap . diff    (             ) ;
			
            if( diff>best_eval )
            {
                best_eval  =  diff                ;
                best       =  permutation.clone() ;
            }
        }

        best . clip ( M );
    }

    static void read_data() throws Throwable
    {
               in   =  new Fio       (   System.in   ) ;
               N    =  in . nextInt  (               ) ;
               M    =  in . nextInt  (               ) ;
        int[]  LOW  =  new int [ M ]                   ;
        int[]  HI   =  new int [ M ]                   ;
	
        for( int i=0; i<M; i++ )  LOW [ i ]  =  in . nextInt();
        for( int i=0; i<M; i++ )   HI [ i ]  =  in . nextInt();
        
        generator    =  new PermutationGenerator( LOW , HI , N );
    }

}