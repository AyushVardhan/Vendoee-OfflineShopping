<?php
	define('HOST','localhost');
	define('USER','psVishalPs');
	define('PASS','vendoe1232');
	define('DB','vendoe');

        try {
                           
                 $conn = new PDO("mysql:host=localhost;dbname=vendoe", USER, PASS);
    
                 $conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
               
                     $id = 0;
               $main_cat = $_POST['maincat'];
               $sub_cat = $_POST['subcat'];

               $sth = $conn->prepare("SELECT id FROM seller_categories WHERE name='$main_cat'");
               
if($sth->execute()) {
     
 if($sth->rowCount() > 0) {
  
  while($result = $sth->fetchObject()) {
      $id = $result->id;
          }
        }
    } 
    
  
$sth1 = $conn->prepare("SELECT id FROM product_categories WHERE seller_category_id=$id and name='$sub_cat'");
               
if($sth1->execute()) {
     
 if($sth1->rowCount() > 0) {
  
  while($result1 = $sth1->fetchObject()) {
      echo $result1->id.",".$id;
          }
        }
    }     

            }
        catch(PDOException $e)
            {
             echo "Error is: " . $e->getMessage();
             }

	  mysqli_close($con);
?>