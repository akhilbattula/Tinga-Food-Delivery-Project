<?php 
	
//If a post request is detected 
if($_SERVER['REQUEST_METHOD']=='POST'){

	//Getting the username and otp 
	$user = $_POST['user'];
	$otp = $_POST['otp'];

    //Importing the dbConnect script 
	require_once('./config.php');
	
	//Creating an SQL to fetch the otp from the table 
	$sql = "SELECT otp, created_at FROM `tbl_otp` WHERE username = '$user'";
	
	//Getting the result array from database 
	$result = mysqli_fetch_array(mysqli_query($conn,$sql));
	
	//Getting the otp from the array 
	$realotp = $result['otp'];
  	
  $created_at = strtotime($result['created_at']);
	//If the otp given is equal to otp fetched from database 
	if($otp == $realotp){
    
    $date = date('Y-m-d H:i:s');
    $checkTime = strtotime($date);

    $diff = $checkTime - $created_at;
    $minutes = round(abs($diff) / 60,3);
    
    if($diff > 2){
    
 			  echo '{"success":"0","Message":"OTP expired"}';

    }else{
    		//Creating an sql query to update the column verified to 1 for the specified user 
    		$sql_update = "UPDATE `tbl_otp` SET  verified =  '1' WHERE username ='$user'";
    		
    		//If the table is updated 
    		if(mysqli_query($conn,$sql_update)){
    			//displaying success 
      			//echo 'success';
      			echo '{"success":"1","Message":"OTP verified"}';

    		}else{
    			//displaying failure 
      			//echo 'failure';
      			echo '{"success":"0","Message":"Server issue"}';

    		}

    } 
	}else{
		//displaying failure if otp given is not equal to the otp fetched from database  
      echo '{"success":"0","Message":"Incorrect OTP"}';
	}
	
	//Closing the database 
	mysqli_close($conn);
}