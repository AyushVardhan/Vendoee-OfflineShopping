<?php
	define('HOST','localhost');
	define('USER','psVishalPs');
	define('PASS','vendoe1232');
	define('DB','vendoe');
	
	$conn = new PDO("mysql:host=localhost;dbname=vendoe", USER, PASS);
        $conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

	$OTP = $_POST['OPT'];
	$sessionId = $_POST['sessionId'];
	$phoneNo = $_POST['phoneNo'];
	//$APIKey = "9e5cb388-08ab-11e7-9462-00163ef91450";
	$APIKey = "fb223f2d-0cde-11e7-9462-00163ef91450";
	
	//API URL
    	$url = "https://2factor.in/API/V1/".$APIKey."/SMS/VERIFY/".$sessionId."/".$OTP;
 
// init the resource
    $ch = curl_init();
    curl_setopt_array($ch, array(
        CURLOPT_URL => $url,
        CURLOPT_RETURNTRANSFER => true,
        CURLOPT_POST => true,
        CURLOPT_POSTFIELDS => $postData
            //,CURLOPT_FOLLOWLOCATION => true
    ));
 
 
    //Ignore SSL certificate verification
    curl_setopt($ch, CURLOPT_SSL_VERIFYHOST, 0);
    curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, 0);
 
 
    //get response
    $output = curl_exec($ch);
    
    $arr = (array) json_decode($output, true);
    foreach ($arr as $k=>$v){
    	if($v == "Success"){
    	
		        $sth5 = $conn->prepare("select * from seller_contacts where phone_no= '".$phoneNo."'");
      			$sth5->execute();
      			
   			$sth4 = $conn->prepare("select * from customers where phone_no= '".$phoneNo."'");
      			$sth4->execute(); 
      			
      			      	if(($sth4->rowCount() > 0)){
      				      	echo "Customer";   
	
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
	
      				      	echo "Retailer";      				
      				
      				}else{
      					$sth = $conn->prepare("INSERT INTO customers(phone_no)VALUES(:ph)");
		                        $sth->bindParam(':ph',$phoneNo);
		                        $sth->execute();
      				}      			   	  
    	}
    	break;    
    }
    
    echo $output;
 
    //Print error if any
    if (curl_errno($ch)) {
        echo 'error:' . curl_error($ch);
    }
    
    curl_close($ch);

?>