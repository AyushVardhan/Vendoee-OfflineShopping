<?php
define('HOST','localhost');
define('USER','psVishalPs');
define('PASS','vendoe1232');
define('DB','vendoe');


	$con = mysqli_connect(HOST,USER,PASS,DB);

	$sql = "select * from Persons";

	$res = mysqli_query($con,$sql);

	$result = array();

	while($row = mysqli_fetch_array($res)){
		array_push($result,
		array('name'=>$row[0],
		'address'=>$row[1]
		));
	}

	echo json_encode(array("result"=>$result));

mysqli_close($con);

?>
