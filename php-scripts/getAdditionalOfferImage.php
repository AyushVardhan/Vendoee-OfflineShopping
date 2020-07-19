<?php
define('HOST','localhost');
define('USER','psVishalPs');
define('PASS','vendoe1232');
define('DB','vendoe');


	$con = mysqli_connect(HOST,USER,PASS,DB);
	

	$id = $_POST['oid'];

	$sql = "SELECT * FROM  offer_images WHERE offer_id = $id LIMIT 2";

        $res = mysqli_query($con,$sql);

        $result = array();

        while($row = mysqli_fetch_array($res)){
                array_push($result,
                array('id'=>$row[1],
                'img'=>$row[2]
                ));
        }

        echo json_encode(array("result"=>$result));
	
	mysqli_close($con);

?>