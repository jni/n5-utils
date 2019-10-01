/**
 *                         THE CRAPL v0 BETA 1
 *
 *
 * 0. Information about the CRAPL
 *
 * If you have questions or concerns about the CRAPL, or you need more
 * information about this license, please contact:
 *
 *    Matthew Might
 *    http://matt.might.net/
 *
 *
 * I. Preamble
 *
 * Science thrives on openness.
 *
 * In modern science, it is often infeasible to replicate claims without
 * access to the software underlying those claims.
 *
 * Let's all be honest: when scientists write code, aesthetics and
 * software engineering principles take a back seat to having running,
 * working code before a deadline.
 *
 * So, let's release the ugly.  And, let's be proud of that.
 *
 *
 * II. Definitions
 *
 * 1. "This License" refers to version 0 beta 1 of the Community
 *     Research and Academic Programming License (the CRAPL).
 *
 * 2. "The Program" refers to the medley of source code, shell scripts,
 *     executables, objects, libraries and build files supplied to You,
 *     or these files as modified by You.
 *
 *    [Any appearance of design in the Program is purely coincidental and
 *     should not in any way be mistaken for evidence of thoughtful
 *     software construction.]
 *
 * 3. "You" refers to the person or persons brave and daft enough to use
 *     the Program.
 *
 * 4. "The Documentation" refers to the Program.
 *
 * 5. "The Author" probably refers to the caffeine-addled graduate
 *     student that got the Program to work moments before a submission
 *     deadline.
 *
 *
 * III. Terms
 *
 * 1. By reading this sentence, You have agreed to the terms and
 *    conditions of this License.
 *
 * 2. If the Program shows any evidence of having been properly tested
 *    or verified, You will disregard this evidence.
 *
 * 3. You agree to hold the Author free from shame, embarrassment or
 *    ridicule for any hacks, kludges or leaps of faith found within the
 *    Program.
 *
 * 4. You recognize that any request for support for the Program will be
 *    discarded with extreme prejudice.
 *
 * 5. The Author reserves all rights to the Program, except for any
 *    rights granted under any additional licenses attached to the
 *    Program.
 *
 *
 * IV. Permissions
 *
 * 1. You are permitted to use the Program to validate published
 *    scientific claims.
 *
 * 2. You are permitted to use the Program to validate scientific claims
 *    submitted for peer review, under the condition that You keep
 *    modifications to the Program confidential until those claims have
 *    been published.
 *
 * 3. You are permitted to use and/or modify the Program for the
 *    validation of novel scientific claims if You make a good-faith
 *    attempt to notify the Author of Your work and Your claims prior to
 *    submission for publication.
 *
 * 4. If You publicly release any claims or data that were supported or
 *    generated by the Program or a modification thereof, in whole or in
 *    part, You will release any inputs supplied to the Program and any
 *    modifications You made to the Progam.  This License will be in
 *    effect for the modified program.
 *
 *
 * V. Disclaimer of Warranty
 *
 * THERE IS NO WARRANTY FOR THE PROGRAM, TO THE EXTENT PERMITTED BY
 * APPLICABLE LAW. EXCEPT WHEN OTHERWISE STATED IN WRITING THE COPYRIGHT
 * HOLDERS AND/OR OTHER PARTIES PROVIDE THE PROGRAM "AS IS" WITHOUT
 * WARRANTY OF ANY KIND, EITHER EXPRESSED OR IMPLIED, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE. THE ENTIRE RISK AS TO THE QUALITY AND
 * PERFORMANCE OF THE PROGRAM IS WITH YOU. SHOULD THE PROGRAM PROVE
 * DEFECTIVE, YOU ASSUME THE COST OF ALL NECESSARY SERVICING, REPAIR OR
 * CORRECTION.
 *
 *
 * VI. Limitation of Liability
 *
 * IN NO EVENT UNLESS REQUIRED BY APPLICABLE LAW OR AGREED TO IN WRITING
 * WILL ANY COPYRIGHT HOLDER, OR ANY OTHER PARTY WHO MODIFIES AND/OR
 * CONVEYS THE PROGRAM AS PERMITTED ABOVE, BE LIABLE TO YOU FOR DAMAGES,
 * INCLUDING ANY GENERAL, SPECIAL, INCIDENTAL OR CONSEQUENTIAL DAMAGES
 * ARISING OUT OF THE USE OR INABILITY TO USE THE PROGRAM (INCLUDING BUT
 * NOT LIMITED TO LOSS OF DATA OR DATA BEING RENDERED INACCURATE OR
 * LOSSES SUSTAINED BY YOU OR THIRD PARTIES OR A FAILURE OF THE PROGRAM
 * TO OPERATE WITH ANY OTHER PROGRAMS), EVEN IF SUCH HOLDER OR OTHER
 * PARTY HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 *
 *
 */
package org.janelia.saalfeldlab;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;

import org.janelia.saalfeldlab.googlecloud.GoogleCloudClientSecretsCmdLinePrompt;
import org.janelia.saalfeldlab.googlecloud.GoogleCloudClientSecretsPrompt;
import org.janelia.saalfeldlab.googlecloud.GoogleCloudOAuth;
import org.janelia.saalfeldlab.googlecloud.GoogleCloudResourceManagerClient;
import org.janelia.saalfeldlab.googlecloud.GoogleCloudStorageClient;
import org.janelia.saalfeldlab.googlecloud.GoogleCloudStorageURI;
import org.janelia.saalfeldlab.n5.Compression;
import org.janelia.saalfeldlab.n5.N5FSReader;
import org.janelia.saalfeldlab.n5.N5FSWriter;
import org.janelia.saalfeldlab.n5.N5Reader;
import org.janelia.saalfeldlab.n5.N5Writer;
import org.janelia.saalfeldlab.n5.googlecloud.N5GoogleCloudStorageReader;
import org.janelia.saalfeldlab.n5.googlecloud.N5GoogleCloudStorageWriter;
import org.janelia.saalfeldlab.n5.hdf5.N5HDF5Reader;
import org.janelia.saalfeldlab.n5.hdf5.N5HDF5Writer;
import org.janelia.saalfeldlab.n5.s3.N5AmazonS3Reader;
import org.janelia.saalfeldlab.n5.s3.N5AmazonS3Writer;
import org.janelia.saalfeldlab.n5.zarr.N5ZarrReader;
import org.janelia.saalfeldlab.n5.zarr.N5ZarrWriter;

import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.AmazonS3URI;
import com.google.auth.Credentials;
import com.google.cloud.resourcemanager.Project;
import com.google.cloud.resourcemanager.ResourceManager;
import com.google.cloud.storage.Storage;

import ch.systemsx.cisd.hdf5.HDF5Factory;

/**
*
*
* @author Igor Pisarev &lt;pisarevi@janelia.hhmi.org&gt;
*/
public class N5Factory {

	public static class N5Options {

		public final String containerPath;
		public final int[] blockSize;
		public final Compression compression;

		public N5Options(final String containerPath, final int[] blockSize, final Compression compression) {
			this.containerPath = containerPath;
			this.blockSize = blockSize;
			this.compression = compression;
		}
	}

	private static enum N5AccessType {

		Reader,
		Writer
	}

	public static N5Reader createN5Reader(final N5Options options) throws IOException {

		return createN5(options, N5AccessType.Reader);
	}

	public static N5Writer createN5Writer(final N5Options options) throws IOException {

		return createN5(options, N5AccessType.Writer);
	}

	@SuppressWarnings("unchecked")
	private static <N5 extends N5Reader> N5 createN5(final N5Options options, final N5AccessType accessType) throws IOException {

		URI uri = null;
		try {
			uri = URI.create(options.containerPath);
		} catch (final IllegalArgumentException e) {}
		if (uri == null || uri.getScheme() == null) {
			if (isHDF5(options.containerPath, accessType))
				return (N5) createN5HDF5(options.containerPath, options.blockSize, accessType);
			else if (isZarr(options.containerPath))
				return (N5) createN5Zarr(options.containerPath, accessType);
			else
				return (N5) createN5FS(options.containerPath, accessType);
		}

		if (uri.getScheme().equalsIgnoreCase("http") || uri.getScheme().equalsIgnoreCase("https")) {
			// s3 uri parser is capable of parsing http links, try to parse it first as an s3 uri
			AmazonS3URI s3Uri;
			try {
				s3Uri = new AmazonS3URI(uri);
			} catch (final Exception e) {
				s3Uri = null;
			}

			if (s3Uri != null) {
				if (s3Uri.getBucket() == null || s3Uri.getBucket().isEmpty() || (s3Uri.getKey() != null && !s3Uri.getKey().isEmpty()))
					throw new IllegalArgumentException("N5 datasets on AWS S3 are stored in buckets. Please provide a link to a bucket.");
				return (N5) createN5S3(options.containerPath, accessType);
			} else {
				// might be a google cloud link
				final GoogleCloudStorageURI googleCloudUri;
				try {
					googleCloudUri = new GoogleCloudStorageURI(uri);
				} catch (final Exception e) {
					throw new IllegalArgumentException("Expected either a local path or a link to AWS S3 bucket / Google Cloud Storage bucket.");
				}

				if (googleCloudUri.getBucket() == null || googleCloudUri.getBucket().isEmpty() || (googleCloudUri.getKey() != null && !googleCloudUri.getKey().isEmpty()))
					throw new IllegalArgumentException("N5 datasets on Google Cloud are stored in buckets. Please provide a link to a bucket.");
				return (N5) createN5GoogleCloud(options.containerPath, accessType);
			}
		} else {
			switch (uri.getScheme().toLowerCase()) {
			case "file":
				final String parsedPath = Paths.get(uri).toString();
				if (isHDF5(parsedPath, accessType))
					return (N5) createN5HDF5(parsedPath, options.blockSize, accessType);
				else if (isZarr(parsedPath))
					return (N5) createN5Zarr(parsedPath, accessType);
				else
					return (N5) createN5FS(parsedPath, accessType);
			case "s3":
				return (N5) createN5S3(options.containerPath, accessType);
			case "gs":
				return (N5) createN5GoogleCloud(options.containerPath, accessType);
			default:
				throw new IllegalArgumentException("Unsupported protocol: " + uri.getScheme());
			}
		}
	}

	@SuppressWarnings("unchecked")
	private static <N5 extends N5FSReader> N5 createN5FS(final String containerPath, final N5AccessType accessType) throws IOException {

		switch (accessType) {
		case Reader:
			return (N5) new N5FSReader(containerPath);
		case Writer:
			return (N5) new N5FSWriter(containerPath);
		default:
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	private static <N5 extends N5HDF5Reader> N5 createN5HDF5(final String containerPath, final int[] blockSize, final N5AccessType accessType) throws IOException {

		switch (accessType) {
		case Reader:
			return (N5) new N5HDF5Reader(HDF5Factory.openForReading(containerPath), blockSize);
		case Writer:
			return (N5) new N5HDF5Writer(HDF5Factory.open(containerPath), blockSize);
		default:
			return null;
		}
	}

	private static <N5 extends N5ZarrReader> N5 createN5Zarr(final String containerPath, final N5AccessType accessType) throws IOException {

		switch (accessType) {
		case Reader:
			return (N5) new N5ZarrReader(containerPath, true);
		case Writer:
			return (N5) new N5ZarrWriter(containerPath, true);
		default:
			return null;
		}
	}

	private static boolean isHDF5(final String containerPath, final N5AccessType accessType) {

		switch (accessType) {
		case Reader:
			return Files.isRegularFile(Paths.get(containerPath));
		case Writer:
			return containerPath.toLowerCase().endsWith(".h5") || containerPath.toLowerCase().endsWith(".hdf5") || containerPath.toLowerCase().endsWith(".hdf");
		default:
			throw new RuntimeException();
		}
	}

	private static boolean isZarr(final String containerPath) {

		return containerPath.toLowerCase().endsWith(".zarr") ||
				(Files.isDirectory(Paths.get(containerPath)) &&
						(Files.isRegularFile(Paths.get(containerPath, ".zarray")) ||
								Files.isRegularFile(Paths.get(containerPath, ".zgroup"))));
	}

	@SuppressWarnings("unchecked")
	private static <N5 extends N5AmazonS3Reader> N5 createN5S3(final String containerPath, final N5AccessType accessType) throws IOException {

		final AmazonS3 s3 = AmazonS3ClientBuilder.standard().withCredentials(new ProfileCredentialsProvider()).build();

		final AmazonS3URI s3Uri = new AmazonS3URI(containerPath);
		final String bucketName = s3Uri.getBucket();

		switch (accessType) {
		case Reader:
			return (N5) new N5AmazonS3Reader(s3, bucketName);
		case Writer:
			return (N5) new N5AmazonS3Writer(s3, bucketName);
		default:
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	private static <N5 extends N5GoogleCloudStorageReader> N5 createN5GoogleCloud(final String containerPath, final N5AccessType accessType) throws IOException {

		final GoogleCloudClientSecretsPrompt clientSecretsPrompt = new GoogleCloudClientSecretsCmdLinePrompt();
		final GoogleCloudOAuth oauth = new GoogleCloudOAuth(clientSecretsPrompt);
		final Credentials credentials = oauth.getCredentials();

		final GoogleCloudStorageClient storageClient;
		switch (accessType) {
		case Reader:
			storageClient = new GoogleCloudStorageClient(credentials);
			break;
		case Writer:
			storageClient = new GoogleCloudStorageClient(credentials, getGoogleCloudProjectId(credentials));
			break;
		default:
			storageClient = null;
		}
		final Storage storage = storageClient.create();

		final GoogleCloudStorageURI googleCloudUri = new GoogleCloudStorageURI(containerPath);
		final String bucketName = googleCloudUri.getBucket();

		switch (accessType) {
		case Reader:
			return (N5) new N5GoogleCloudStorageReader(storage, bucketName);
		case Writer:
			return (N5) new N5GoogleCloudStorageWriter(storage, bucketName);
		default:
			return null;
		}
	}

	private static String getGoogleCloudProjectId(final Credentials credentials) {

		// FIXME: get first project id for now
		// TODO: prompt user for project id
		final ResourceManager resourceManager = new GoogleCloudResourceManagerClient(credentials).create();
		final Iterator<Project> projectsIterator = resourceManager.list().iterateAll().iterator();
		if (!projectsIterator.hasNext())
			throw new RuntimeException("No projects were found. Create a google cloud project first");
		final String projectId = projectsIterator.next().getProjectId();

		return projectId;
	}
}
