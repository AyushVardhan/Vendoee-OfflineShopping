<?php

        define('HOST','localhost');
        define('USER','psVishalPs');
        define('PASS','vendoe1232');
        define('DB','vendoe');

        try {
                 $conn = new PDO("mysql:host=localhost;dbname=vendoe", USER, PASS);
    
                 $conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
                       
			
                        $sql = "SELECT * FROM vouchers";
                        $STH = $conn->prepare($sql);
                        $STH -> execute();
                        
                        while($result = $STH -> fetch()){

						$vid = $result ["id"];
						$pname = $result ["name"];
						$image = $result ["image"];
						$points = $result ["points"];	
                        			
                        	  echo $pname."||".$image."||".$points."||".$vid.";";			
                        }
            }
        catch(PDOException $e)
            {
             echo "Error is: " . $e->getMessage();
             }

?>