<?php

        define('HOST','localhost');
        define('USER','psVishalPs');
        define('PASS','vendoe1232');
        define('DB','vendoe');

	$con = mysqli_connect(HOST,USER,PASS,DB);
	
	$main_cat = $_POST['id'];
	
	$sql = "select * from product_categories where seller_category_id = $main_cat";

        $res = mysqli_query($con,$sql);

        $result = array();

        while($row = mysqli_fetch_array($res)){
                array_push($result,
                array('id'=>$row[0],
                'category'=>$row[2]
                ));
        }

        echo json_encode(array("result"=>$result));

	mysqli_close($con);
?>