package com.swp391.onlinetutorapplication.onlinetutorapplication;


import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.ListFolderResult;
import com.dropbox.core.v2.files.Metadata;
import com.dropbox.core.v2.users.DbxUserUsersRequests;
import com.dropbox.core.v2.users.FullAccount;
import com.swp391.onlinetutorapplication.onlinetutorapplication.service.courseService.courseServiceInterface.CourseServiceInterface;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;


@SpringBootApplication
public class OnlineTutorApplication {

	public static void main(String[] args) {
		SpringApplication.run(OnlineTutorApplication.class, args);
	}
//	@Bean
//	CommandLineRunner run(CourseServiceInterface courseService){
//		return args -> {
////			String access_token = "grW4Z3mk2P8AAAAAAAAAARteeFq71F9bZvsEgmcH5JszBU1cAguiekc9zl0HqBB4";
////			DbxRequestConfig config = new DbxRequestConfig("dropbox/SWP391");
////			DbxClientV2 client = new DbxClientV2(config,access_token);
////			FullAccount account;
////			DbxUserUsersRequests userUsersRequests = client.users();
////			account = userUsersRequests.getCurrentAccount();
////			System.out.println(account.getName().getDisplayName());
////
////			try{
////				ListFolderResult listFolderResult = client.files().listFolder("/Course-Material");
////				while (true){
////					for(Metadata metadata : listFolderResult.getEntries()){
////						System.out.println(metadata.getPathLower());
////
////					}
////					if(!listFolderResult.getHasMore()){
////						break;
////					}
////					listFolderResult = client.files().listFolderContinue(listFolderResult.getCursor());
////				}
////			}catch (DbxException e){
////				e.printStackTrace();
////			}
//
//			courseService.uploadMaterial(1,'/');
//		};
//	}

}
