<?php

        define('HOST','localhost');
        define('USER','psVishalPs');
        define('PASS','vendoe1232');
        define('DB','vendoe');

	$con = mysqli_connect(HOST,USER,PASS,DB);
	

	$id = $_POST['id'];

	$sql = "select seller_category_id,cat_name from seller_intermediate_categories where seller_id = $id";

        $res = mysqli_query($con,$sql);

        $result = array();

        while($row = mysqli_fetch_array($res)){
                array_push($result,
                array('id'=>$row[0],
                'category'=>$row[1]
                ));
        }

        echo json_encode(array("result"=>$result));
	
	mysqli_close($con);
?>
