<?php 

  //include("./config.php");
	//Constants for our API
	//this is applicable only when you are using Cheap SMS Bazaar
	define('SMSUSER','your user name');
	define('PASSWORD','your password');
	define('SENDERID','EMPIRE');
	
	
	//This function will send the otp 
	function sendOtp($otp, $phone){
		//This is the sms text that will be sent via sms 
		$sms_content = "Your OTP for Tinga Mobile verification is $otp";
		
		//Encoding the text in url format
		$sms_text = urlencode($sms_content);

		//This is the Actual API URL concatnated with required values 
   
		$api_url = 'https://www.fast2sms.com/dev/bulk?authorization=JjBE7DtOCur73iBavyaI3cMdFDcRgRcqxHHcctNjqAITOi9iWuyk9acjGMPT&sender_id=FSTSMS&message='.$sms_text.'&language=english&route=qt&numbers='.$phone;
		
		//Envoking the API url and getting the response 
		//$response = file_get_contents($api_url);
		$ch = curl_init();
    curl_setopt($ch, CURLOPT_URL, $api_url);
    curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, false);
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
    curl_setopt($ch, CURLOPT_FOLLOWLOCATION, true );
    // This is what solved the issue (Accepting gzip encoding)
    curl_setopt($ch, CURLOPT_ENCODING, "gzip,deflate");     
    $response = curl_exec($ch);
    curl_close($ch);
    //echo $response;
    //echo $response;
		//Returning the response 
		return $response;
	}
	
	
	//If a post request comes to this script 
	if($_SERVER['REQUEST_METHOD']=='POST'){	
		//getting username password and phone number 
		$user = $_POST['user'];
		//$password = $_POST['password'];
		$phone = $_POST['phone'];

    $date = date('Y-m-d H:i:s');
		
		//Generating a 6 Digits OTP or verification code 
		$otp = rand(100000, 999999);
		
		//Importing the db connection script 
		require_once('./config.php');
		
		//Creating an SQL Query 
		$sql = "INSERT INTO `tbl_otp` (username, phone, otp,created_at) values ('$user','$phone','$otp','$date')";
		
		//If the query executed on the db successfully 
		if(mysqli_query($conn,$sql)){
			//printing the response given by sendOtp function by passing the otp and phone number 
            echo sendOtp($otp,$phone);
		}else{
			//printing the failure message in json 
			echo '{"ErrorMessage":"Failure"}';
		}
		
		//Closing the database connection 
		mysqli_close($conn);
	}