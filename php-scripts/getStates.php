<?php
	define('HOST','localhost');
	define('USER','psVishalPs');
	define('PASS','vendoe1232');
	define('DB','vendoe');

        try {
                 $conn = new PDO("mysql:host=localhost;dbname=vendoe", USER, PASS);
    
                 $conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
                 $conn->beginTransaction();
             		$city = "";
             		$phoneNo = $_POST['phoneNo'];
             		
		        $sth5 = $conn->prepare("select * from seller_contacts where phone_no= '".$phoneNo."'");
      			$sth5->execute();
      			
   			$sth4 = $conn->prepare("select * from customers where phone_no= '".$phoneNo."'");
      			$sth4->execute(); 
      			
      			      	if(($sth4->rowCount() > 0)){
      				      	//echo "Customer";   
	
      				}elseif(($sth5->rowCount() > 0)){

      					while($result3 = $sth5->fetchObject())
      					{
				      		$sellid= $result3->seller_id;
      					}
      					
      					$sth6 = $conn->prepare("select name,email from sellers where id= ".$sellid);
      					$sth6->execute();

      					while($result4 = $sth6->fetchObject())
      					{
				      		$name= $result4->name;
						$email= $result4->email;
      					}
      					
      				      	$sth = $conn->prepare("INSERT INTO customers(name,email,phone_no)VALUES(:name,:email,:ph)");
	        	                $sth->bindParam(':ph',$phoneNo);
	        	                $sth->bindParam(':name',$name);
	        	                $sth->bindParam(':email',$email);
	        	                	        	                
	                	        $sth->execute();
	
      				      	//echo "Retailer";      				
      				
      				}else{
      					$sth = $conn->prepare("INSERT INTO customers(phone_no)VALUES(:ph)");
		                        $sth->bindParam(':ph',$phoneNo);
		                        $sth->execute();
      				}             		
             		
      			$sth6 = $conn->prepare("select distinct(state) from seller_addresses");
    			  		if($sth6->execute())
      					{
					      	while($result6 = $sth6->fetchObject())
      						{
      							$contact = $result6->state;
      							$city = $city.",".$contact;
      						}
      					}
            
      			echo $city;
			$conn->commit();
            }
        catch(PDOException $e)
            {
             echo "Error is: " . $e->getMessage();
             }

	  mysqli_close($con);
?>