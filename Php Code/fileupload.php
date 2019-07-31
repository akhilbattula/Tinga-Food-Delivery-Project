<?php 
	
  include "./config.php";
  mysqli_set_charset($conn, 'utf8mb4');
		
	//Getting the server ip 
	$server_ip = gethostbyname(gethostname());
	
	//creating the upload url 
	$upload_url = 'http://'.$server_ip.'/images/'; 
	
 echo $upload_url;
	//response array 
	$response = array(); 
	
	if($_SERVER['REQUEST_METHOD']=='POST'){
		
		//checking the required parameters from the request 
		if(isset($_POST['name']) and isset($_FILES['image']['name'])){
			
			//getting name from the request 
			$name = $_POST['name'];
			
			//getting file info from the request 
			$fileinfo = pathinfo($_FILES['image']['name']);
			
			//getting the file extension 
			$extension = $fileinfo['extension'];
			
			//file url to store in the database 
			$file_url = $upload_url . '1'.'.' . $extension;
			
			//file path to upload in the server 
			$file_path = $upload_path .'1'. '.'. $extension; 
      
      echo $file_path;
			
		//trying to save the file in the directory 
			try{
				//saving the file 
				move_uploaded_file($_FILES['image']['tmp_name'],$file_path);

        if (is_uploaded_file($_FILES['image']['tmp_name'])){
    				$response['error']=false;
    				$response['message']="upload success";
        
        }else{
    				$response['error']=true;

        }
			}catch(Exception $e){
				$response['error']=true;
				$response['message']=$e->getMessage();
			}		
			//displaying the response 
			echo json_encode($response);
			
			//closing the connection 
			mysqli_close($conn);
		}else{
			$response['error']=true;
			$response['message']='Please choose a file';
		}
	}

	 